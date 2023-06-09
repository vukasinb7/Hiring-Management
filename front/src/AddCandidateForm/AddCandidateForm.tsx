import { useForm } from '@mantine/form';
import {TextInput, Button, Box, MultiSelect} from '@mantine/core';
import {DatePickerInput} from "@mantine/dates";
import {Dispatch, SetStateAction, useEffect, useState} from "react";
import axios from "axios";
import './AddCandidateForm.css'
import {RowData, SkillPreview} from "../models/models";
import { notifications } from '@mantine/notifications';
import {IconCheck} from "@tabler/icons-react";

type MyProps = {
    onClick: (candidates: RowData[]) => void;
    onClose: () => void;
    id:number;
}

function AddCandidateForm(props:MyProps) {
    const [data,setData]=useState<SkillPreview[]>([]);
    const [userID,setUserID]=useState<number>(-1);
    const [disabled,setDisabled]=useState<boolean>(false);
    const form = useForm({
        initialValues: { name: '', email: '', phone: '',birth:new Date(),skills:[] as number[] },validateInputOnBlur:true,
        validate: {
            name: (value) => (value.length < 2 ? 'Name must have at least 2 letters' : null),
            email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email'),
            phone: (value) => (/^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{3,6}$/.test(value) ? null : 'Invalid phone'),
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
                setDisabled(true);
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
                notifications.show({
                    title: 'Candidate successfully added!',
                    message: '',
                    icon:<IconCheck size="1.2rem" />,
                    styles: (theme) => ({
                        root: {
                            backgroundColor: theme.colors.blue[6],
                            borderColor: theme.colors.blue[6],

                            '&::before': { backgroundColor: theme.white },
                        },
                        title: { color: theme.white },
                        description: { color: theme.white },

                        closeButton: {
                            color: theme.white,
                            '&:hover': { backgroundColor: theme.colors.blue[7] },
                        }
                    }),
                })
                props.onClose();
            });}
        )}
        else {
            axios.put(`http://localhost:9000/api/candidate/skills/update/${userID}`,
                {skillIds:values.skills}).then(()=>{
                axios.get("http://localhost:9000/api/candidate/all?page=0&size=10").then(response=>{
                    props.onClick(response.data.candidates);
                    notifications.show({
                        title: 'Candidate successfully updated!',
                        message: '',
                        icon:<IconCheck size="1.2rem" />,
                        styles: (theme) => ({
                            root: {
                                backgroundColor: theme.colors.blue[6],
                                borderColor: theme.colors.blue[6],

                                '&::before': { backgroundColor: theme.white },
                            },
                            title: { color: theme.white },
                            description: { color: theme.white },

                            closeButton: {
                                color: theme.white,
                                '&:hover': { backgroundColor: theme.colors.blue[7] },
                            }
                        }),
                    })
                    props.onClose();
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
                notifications.show({
                    title: 'Candidate successfully deleted!',
                    message: '',
                    icon:<IconCheck size="1.2rem" />,
                    styles: (theme) => ({
                        root: {
                            backgroundColor: theme.colors.blue[6],
                            borderColor: theme.colors.blue[6],

                            '&::before': { backgroundColor: theme.white },
                        },
                        title: { color: theme.white },
                        description: { color: theme.white },

                        closeButton: {
                            color: theme.white,
                            '&:hover': { backgroundColor: theme.colors.blue[7] },
                        }
                    }),
                })
                props.onClose();
            });
            })
    }

    return (
        <Box maw={320} mx="auto">
            <form onSubmit={form.onSubmit(values => {sendCandidate(values,props)})}>
                <TextInput label="Name" placeholder="Name"  disabled={disabled} {...form.getInputProps('name')}/>
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
                        {disabled?"Update":"Add"}
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