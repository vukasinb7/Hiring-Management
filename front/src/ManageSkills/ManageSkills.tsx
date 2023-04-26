import { useForm } from '@mantine/form';
import {TextInput, Button, Box, MultiSelect, Text, ActionIcon} from '@mantine/core';
import {useEffect, useState} from "react";
import axios from "axios";
import AddIcon from '@mui/icons-material/Add';
import './ManageSkills.css';

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
    closeModal: () => void;
}


function ManageSkills() {
    const [data,setData]=useState<SkillPreview[]>([]);

    let temp=-1;
    const form = useForm({
        initialValues: { name: ''},
        validate: {
            name: (value) => (value.length < 2 ? 'Name must have at least 2 letters' : null)
        },
    });
    useEffect( ()=>{
        axios.get("http://localhost:9000/api/skill/all").then(response=>{
            let dataList:SkillPreview[]=[];
            for(let i=0; i<response.data.length; i++){
                dataList.push({value:response.data[i].id,label:response.data[i].name});
            }
            setData(dataList);

        });
    },[])

    function deleteSkill(id:string):void{
        axios.delete(`http://localhost:9000/api/skill/${id}`).then(()=>{
            axios.get("http://localhost:9000/api/skill/all").then(response=>{
                let dataList:SkillPreview[]=[];
                for(let i=0; i<response.data.length; i++){
                    dataList.push({value:response.data[i].id,label:response.data[i].name});
                }
                setData(dataList);

            });

        });

    }

    let content=data.map(item=>(
        <div className="option-div" key={item.value}>
            <span>{item.label}</span>
            <Button key={item.value} onClick={()=>{deleteSkill(item.value)}}>Delete</Button>
        </div>
    ))

    function addSkill(values: any) {
        axios.post("http://localhost:9000/api/skill",
            {name:values.name}).then(()=>{
            axios.get("http://localhost:9000/api/skill/all").then(response=>{
                let dataList:SkillPreview[]=[];
                for(let i=0; i<response.data.length; i++){
                    dataList.push({value:response.data[i].id,label:response.data[i].name});
                }
                setData(dataList);
                form.reset();

            });}
        ).catch(()=>{
            form.setFieldError("name","Name already exist")
        })
    }

    return (
        <>
            <form onSubmit={form.onSubmit(values => {addSkill(values)})}>
                <TextInput placeholder="Enter skill name..." {...form.getInputProps('name')}
                rightSection={<ActionIcon type="submit">
                    <AddIcon/>
                </ActionIcon>}/>

            </form>
            {content.length > 0 ? (
                content
            ) : (

                        <Text weight={500} align="center">
                            Nothing found
                        </Text>
            )}
        </>
    );
}
export default ManageSkills;