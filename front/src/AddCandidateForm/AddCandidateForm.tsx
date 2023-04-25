import { useForm } from '@mantine/form';
import {TextInput, Button, Box, MultiSelect} from '@mantine/core';
import {DatePickerInput} from "@mantine/dates";
import {useEffect, useState} from "react";
import axios from "axios";

interface Skill{
    value:string;
    label:string;
}
function AddCandidateForm() {
    const [data,setData]=useState<Skill[]>([]);
    const form = useForm({
        initialValues: { name: '', email: '', phone: '',birth:new Date(),skills:[] },validateInputOnBlur:true,

        validate: {
            name: (value) => (value.length < 2 ? 'Name must have at least 2 letters' : null),
            email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email'),
            phone: (value) => (/^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/.test(value) ? null : 'Invalid phone'),
        },
    });
    useEffect( ()=>{
        axios.get("http://localhost:9000/api/skill/all").then(response=>{
            let dataList:Skill[]=[];
            for(let i=0; i<response.data.length; i++){
               dataList.push({value:response.data[i].id,label:response.data[i].name});
            }
            setData(dataList);

        });
    },[])

    return (
        <Box maw={320} mx="auto">
            <form onSubmit={form.onSubmit(values => {console.log(values.birth.toISOString().split('T')[0]);axios.post("http://localhost:9000/api/candidate",{name:values.name,email:values.email,birth:values.birth.toISOString().split('T')[0],phone:values.phone,skills:values.skills})})}>
                <TextInput label="Name" placeholder="Name" {...form.getInputProps('name')} />
                <TextInput label="Email" placeholder="Email" {...form.getInputProps('email')} />
                <TextInput label="Phone" placeholder="Phone" {...form.getInputProps('phone')}/>
                <DatePickerInput
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
                    {...form.getInputProps('skills')}
                />
                <Button type="submit" mt="sm">
                    Submit
                </Button>
            </form>
        </Box>
    );
}
export default AddCandidateForm;