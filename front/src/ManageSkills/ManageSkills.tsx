import { useForm } from '@mantine/form';
import {TextInput, Button, Box, MultiSelect, Text, ActionIcon} from '@mantine/core';
import {useEffect, useState} from "react";
import axios from "axios";
import AddIcon from '@mui/icons-material/Add';
import './ManageSkills.css';
import {RowData, SkillPreview} from "../models/models";



type MyProps = {
    onClick: (candidates: RowData[]) => void;
}


function ManageSkills(props:MyProps) {
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
                axios.get("http://localhost:9000/api/candidate/all?page=0&size=10").then(response=>{
                    props.onClick(response.data.candidates);
                    form.reset();
                });

            });

        });

    }

    let content=data.map(item=>(
        <div className="option-div" key={item.value}>
            <span>{item.label}</span>
            <Button className="delete-buttons" key={item.value} onClick={()=>{deleteSkill(item.value)}}>Delete</Button>
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
        < Box h={700}>
            <form onSubmit={form.onSubmit(values => {addSkill(values)})}>
                <TextInput className="sticky-input" size="md" placeholder="Enter skill name..." {...form.getInputProps('name')}
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
        </Box>
    );
}
export default ManageSkills;