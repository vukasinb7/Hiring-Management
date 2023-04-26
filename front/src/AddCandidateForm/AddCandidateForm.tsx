import { useForm } from '@mantine/form';
import {TextInput, Button, Box, MultiSelect} from '@mantine/core';
import {DatePickerInput} from "@mantine/dates";
import {Dispatch, SetStateAction, useEffect, useState} from "react";
import axios from "axios";
import './AddCandidateForm.css'
interface SkillPreview{
    value:string;
    label:string;
}

interface RowData {
    name: string;
    email: string;
    contactNumber: string;
    birth: number[];
    skills:Skill[];
    id:number;
}
interface Skill{
    name:string;
    id:number;
}
type MyProps = {
    onClick: (candidates: RowData[]) => void;
    id:number;

}


function AddCandidateForm(props:MyProps) {
    const [data,setData]=useState<SkillPreview[]>([]);
    const [selectedValue,setSelectedValue]=useState<SkillPreview[]>([]);
    const [userID,setUserID]=useState<number>(-1);
    const [disabled,setDisabled]=useState<boolean>(false);
    const form = useForm({
        initialValues: { name: '', email: '', phone: '',birth:new Date(),skills:[] as number[] },validateInputOnBlur:true,

        validate: {
            name: (value) => (value.length < 2 ? 'Name must have at least 2 letters' : null),
            email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email'),
            phone: (value) => (/^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/.test(value) ? null : 'Invalid phone'),
        },
    });
    useEffect( ()=>{
        axios.get("http://localhost:9000/api/skill/all").then(response=>{
            let dataList:SkillPreview[]=[];
            for(let i=0; i<response.data.length; i++){
               dataList.push({value:response.data[i].id,label:response.data[i].name});
            }
            setData(dataList);
            setUserID(props.id);


        });
    },[])
    useEffect(()=>{
        if (userID!=-1){
            setDisabled(true);

            axios.get(`http://localhost:9000/api/candidate/${userID}`).then(response=>{
                form.values.name=response.data.name;
                form.values.email=response.data.email;
                form.values.phone=response.data.contactNumber;
                form.values.birth=new Date(response.data.birth);
                let skillNumbers = [];
                for(let i=0; i<response.data.skills.length; i++){
                    skillNumbers.push(response.data.skills[i].id);
                }
                form.values.skills=skillNumbers;
            });}
    },[userID])

    function sendCandidate(values:any,props:MyProps){
        if (userID==-1){
        axios.post("http://localhost:9000/api/candidate",
            {name:values.name,
                email:values.email,
                birth:values.birth.toISOString().split('T')[0],
                contactNumber:values.phone,
                skillIds:values.skills}).then(()=>{
            axios.get("http://localhost:9000/api/candidate/all?page=0&size=10").then(response=>{
                props.onClick(response.data.candidates);
                form.reset();
            });}
        )}
        else {
            axios.put(`http://localhost:9000/api/candidate/skills/update/${userID}`,
                {skillIds:values.skills}).then(()=>{
                axios.get("http://localhost:9000/api/candidate/all?page=0&size=10").then(response=>{
                    props.onClick(response.data.candidates);
                });
                }
            )
        }

    }
    function deleteCanidate(props:MyProps){
        axios.delete(`http://localhost:9000/api/candidate/${userID}`).then(()=>{
            setUserID(-1);
            axios.get("http://localhost:9000/api/candidate/all?page=0&size=10").then(response=>{
                props.onClick(response.data.candidates);
                form.reset();
            });
            }
        )
    }



    return (
        <Box maw={320} mx="auto">
            <form onSubmit={form.onSubmit(values => {sendCandidate(values,props)})}>
                <TextInput label="Name" placeholder="Name" disabled={disabled} {...form.getInputProps('name')} />
                <TextInput label="Email" placeholder="Email" disabled={disabled} {...form.getInputProps('email')} />
                <TextInput label="Phone" placeholder="Phone" disabled={disabled} {...form.getInputProps('phone')}/>
                <DatePickerInput disabled={disabled}
                    maxDate={new Date()}
                    dropdownType="popover"
                    valueFormat="YYYY-MM-DD"
                    label="Date input"
                    placeholder="Date input"
                    maw={400}
                    mx="auto"
                    {...form.getInputProps('birth')}
                />
                <MultiSelect
                    data={data}
                    label="Candidate's Skills"
                    placeholder="Pick all skills that candidate has"
                    searchable
                    {...form.getInputProps('skills')}
                />
                <div className="submit-div">

                    <Button type="submit" mt="sm">
                        Submit
                    </Button>
                    {userID!=-1&&
                    <Button id="submit-button"  onClick={()=>{deleteCanidate(props)}} mt="sm">
                        Delete
                    </Button>}

                </div>
            </form>
        </Box>
    );

}
export default AddCandidateForm;