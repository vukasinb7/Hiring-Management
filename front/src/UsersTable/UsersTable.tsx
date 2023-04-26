import {useEffect, useState} from 'react';
import {
    createStyles,
    Table,
    ScrollArea,
    UnstyledButton,
    Group,
    Text,
    Center,
    TextInput,
    rem, Modal, Button, MultiSelect, Pagination, Badge, Tooltip,
} from '@mantine/core';
import './UsersTable.css'
import { IconSelector, IconChevronDown, IconChevronUp, IconSearch } from '@tabler/icons-react';
import axios from "axios";
import AddCandidateForm from "../AddCandidateForm/AddCandidateForm";
import {useDisclosure} from "@mantine/hooks";
import {useForm} from "@mantine/form";

const useStyles = createStyles((theme) => ({
    th: {
        padding: '0 !important',
    },

    control: {
        width: '100%',
        padding: `${theme.spacing.xs} ${theme.spacing.md}`,

        '&:hover': {
            backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[6] : theme.colors.gray[0],
        },
    },

    icon: {
        width: rem(21),
        height: rem(21),
        borderRadius: rem(21),
    },
}));

interface AllUsers{
    totalCount:number;
    candidates:RowData[];
}
interface Skill{
    name:string;
    id:number;
}
interface RowData {
    name: string;
    email: string;
    contactNumber: string;
    birth: number[];
    skills:Skill[];
    id:number;
}
interface SkillPreview{
    value:string;
    label:string;
}

interface TableSortProps {
    data: RowData[];
}

interface ThProps {
    children: React.ReactNode;
    reversed: boolean;
    sorted: boolean;
    onSort(): void;
}

function Th({ children, reversed, sorted, onSort }: ThProps) {
    const { classes } = useStyles();
    const Icon = sorted ? (reversed ? IconChevronUp : IconChevronDown) : IconSelector;
    return (
        <th className={classes.th}>
            <UnstyledButton onClick={onSort} className={classes.control}>
                <Group position="apart">
                    <Text fw={500} fz="sm">
                        {children}
                    </Text>
                    <Center className={classes.icon}>
                        <Icon size="0.9rem" stroke={1.5} />
                    </Center>
                </Group>
            </UnstyledButton>
        </th>
    );
}

async function filterData(data: RowData[], search: string,skills:string[],activePage:number,setTotalCount:any) {
    const query = search.toLowerCase().trim();
    if (query!="" && skills.length==0) {
        let response = await axios.get(`http://localhost:9000/api/candidate/searchName?page=${activePage}&size=10&name=` + query);
        setTotalCount(response.data.totalCount);
        return response.data.candidates;
    }
    else if (query=="" && skills.length>0){
        let skillsString="";
        skills.forEach(skillId=>{
            skillsString+="&skills="+skillId;
        })
        let response = await axios.get(`http://localhost:9000/api/candidate/searchSkills?page=${activePage}&size=10`+skillsString);
        setTotalCount(response.data.totalCount);
        return response.data.candidates;
    }
    else if (query!="" && skills.length>0){
        let skillsString="";
        skills.forEach(skillId=>{
            skillsString+="&skills="+skillId;
        })
        let response = await axios.get(`http://localhost:9000/api/candidate/search?page=${activePage}&size=10&name=`+ query+skillsString);
        setTotalCount(response.data.totalCount);
        return response.data.candidates;
    }
    else {
        let response = await axios.get(`http://localhost:9000/api/candidate/all?page=${activePage}&size=10`);
        setTotalCount(response.data.totalCount);
        return response.data.candidates;
    }

}

async function sortData(
    data: RowData[],
    payload: { sortBy: keyof RowData | null; reversed: boolean; search: string;skills:string[],activePage:number,setTotalCount:any }
) {
    const { sortBy } = payload;

    if (!sortBy) {
        return  await filterData(data, payload.search,payload.skills,payload.activePage,payload.setTotalCount);
    }

    return filterData(
        [...data].sort((a, b) => {
            if (payload.reversed) {
                switch (sortBy){
                    case "name":
                        return b.name.localeCompare(a.name);
                    case "email":
                        return b.email.localeCompare(a.email);
                    case "contactNumber":
                        return b.contactNumber.localeCompare(a.contactNumber);
                    case "skills":
                        return b.skills.length>a.skills.length?1:(b.skills.length<a.skills.length?-1:0);
                    default:
                        return 1;
                }
            }
            switch (sortBy){
                case "name":
                    return a.name.localeCompare(b.name);
                case "email":
                    return a.email.localeCompare(b.email);
                case "contactNumber":
                    return a.contactNumber.localeCompare(b.contactNumber);
                case "skills":
                    return a.skills.length>b.skills.length?1:(a.skills.length<b.skills.length?-1:0);
                default:
                    return 1;


            }
        }),
        payload.search,payload.skills,payload.activePage,payload.setTotalCount
    );
}


export function UsersTable() {
    const [openedAddCandidate, { open, close }] = useDisclosure(false);
    const [skills,setSkills]=useState<SkillPreview[]>([]);
    const [activePage, setPage] = useState(1);
    const [id, setId] = useState(-1);
    const [totalCount, setTotalCount] = useState(10);
    const [data,setData]=useState<RowData[]>([]);
    const [search, setSearch] = useState('');
    const [selectedSkills, setSelectedSkills] = useState<string[]>([]);
    const [sortedData, setSortedData] = useState(data);
    const [sortBy, setSortBy] = useState<keyof RowData | null>(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);
    useEffect( ()=>{
        axios.get(`http://localhost:9000/api/candidate/all?page=${activePage}&size=10`).then(response=>{
            setData(response.data.candidates)
        });
    },[activePage])
    useEffect( ()=>{
            sortData(data,{sortBy:"name",reversed:false,search,skills:selectedSkills,activePage:activePage-1,setTotalCount}).then(
                data=>{
                    setSortedData(data)
                }
            )
    },[data]);
    useEffect( ()=>{
        axios.get("http://localhost:9000/api/skill/all").then(response=>{
            let dataList:SkillPreview[]=[];
            for(let i=0; i<response.data.length; i++){
                dataList.push({value:response.data[i].id,label:response.data[i].name});
            }
            setSkills(dataList);

        });
    },[])

    const setSorting = (field: keyof RowData) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
        sortData(data,{sortBy:"name",reversed:false,search,skills:selectedSkills,activePage:activePage-1,setTotalCount}).then(
            data=>{
                setSortedData(data)
            }
        )
    };
    useEffect(()=>{
        sortData(data,{sortBy:"name",reversed:false,search,skills:selectedSkills,activePage:activePage-1,setTotalCount}).then(
            data=>{
                setSortedData(data)
            }
        )
    },[search,selectedSkills]);
    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value } = event.currentTarget;
        setSearch(value);

    };
    function makeTooltip(skills:Skill[]){
        let result="";
        skills.forEach(skill=>{
            result+=skill.name+",";
        })
        result=result.slice(0,result.length-1);
        return result;
    }
    const rows = sortedData.map((row) => (
        <tr key={row.email} onClick={()=>{setId(row.id);open();}}>
            <td>{row.name}</td>
            <td>{row.email}</td>
            <td>{row.contactNumber}</td>
            {row.skills.length>0?
                <td><Tooltip label={makeTooltip(row.skills.slice(1,row.skills.length))}><span><Badge >{row.skills[0].name}</Badge>+{row.skills.length-1}</span></Tooltip></td>:<td>No skills</td>}
        </tr>
    ));
    return (
        <div className="content">
            <Modal className="modal" opened={openedAddCandidate} onClose={close} title="Authentication" centered scrollAreaComponent={ScrollArea.Autosize}>
                <AddCandidateForm onClick={setData} id={id} />
            </Modal>
            <Button onClick={()=>{setId(-1);open();}}>Add Candidate</Button>

            <ScrollArea>
                <div className="search-div">
                <TextInput id="search-name"
                    placeholder="Search candidates by name"
                    size="md"
                    label="Name"
                    icon={<IconSearch size="0.9rem" stroke={1.5} />}
                    value={search}
                    onChange={handleSearchChange}
                />
                <MultiSelect
                    id="search-skill"
                    data={skills}
                    label="Skills"
                    size="md"
                    placeholder="Pick skill that are important to you"
                    searchable
                    nothingFound="Nothing found"
                    value={selectedSkills}
                    onChange={skills=>{setSelectedSkills(skills)}}
                />
                </div>
                <Table className="table" horizontalSpacing="md" verticalSpacing="xs" miw={700} sx={{ tableLayout: 'fixed' }}>
                    <thead>
                    <tr>
                        <Th
                            sorted={sortBy === 'name'}
                            reversed={reverseSortDirection}
                            onSort={() => setSorting('name')}
                        >
                            Name
                        </Th>
                        <Th
                            sorted={sortBy === 'email'}
                            reversed={reverseSortDirection}
                            onSort={() => setSorting('email')}
                        >
                            Email
                        </Th>
                        <Th
                            sorted={sortBy === 'contactNumber'}
                            reversed={reverseSortDirection}
                            onSort={() => setSorting('contactNumber')}
                        >
                            Contact Number
                        </Th>
                        <Th
                            sorted={sortBy === 'skills'}
                            reversed={reverseSortDirection}
                            onSort={() => setSorting('skills')}
                        >
                            Skills
                        </Th>
                    </tr>
                    </thead>
                    <tbody>
                    {rows.length > 0 ? (
                        rows
                    ) : (
                        <tr>
                            <td colSpan={data[0]==null?1:Object.keys(data[0]).length}>
                                <Text weight={500} align="center">
                                    Nothing found
                                </Text>
                            </td>
                        </tr>
                    )}
                    </tbody>
                </Table>
            </ScrollArea>
            <Pagination className="paginator" value={activePage} onChange={setPage} total={Math.ceil(totalCount/10)} />
        </div>
    );
}
export default UsersTable;