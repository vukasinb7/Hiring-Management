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
    rem, Modal, Button, MultiSelect, Pagination, Badge, Tooltip, Select,
} from '@mantine/core';
import './UsersTable.css'
import { IconSelector, IconChevronDown, IconChevronUp, IconSearch } from '@tabler/icons-react';
import axios from "axios";
import AddCandidateForm from "../AddCandidateForm/AddCandidateForm";
import {useDisclosure} from "@mantine/hooks";
import ManageSkills from "../ManageSkills/ManageSkills";
import AddIcon from "@mui/icons-material/Add";
import SettingsIcon from "@mui/icons-material/Settings";
import {RowData, Skill, SkillPreview} from "../models/models";

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
                    <Text className="header-text" fw={500} fz="sm">
                        {children}
                    </Text>

                </Group>
            </UnstyledButton>
        </th>
    );
}

async function filterData(data: RowData[], search: string,skills:string[],activePage:number,setTotalCount:any,pageSize:number) {
    const query = search.toLowerCase().trim();
    if (query!="" && skills.length==0) {
        let response = await axios.get(`http://localhost:9000/api/candidate/searchName?page=${activePage}&size=${pageSize}&name=` + query);
        setTotalCount(response.data.totalCount);
        return response.data.candidates;
    }
    else if (query=="" && skills.length>0){
        let skillsString="";
        skills.forEach(skillId=>{
            skillsString+="&skills="+skillId;
        })
        let response = await axios.get(`http://localhost:9000/api/candidate/searchSkills?page=${activePage}&size=${pageSize}`+skillsString);
        setTotalCount(response.data.totalCount);
        return response.data.candidates;
    }
    else if (query!="" && skills.length>0){
        let skillsString="";
        skills.forEach(skillId=>{
            skillsString+="&skills="+skillId;
        })
        let response = await axios.get(`http://localhost:9000/api/candidate/search?page=${activePage}&size=${pageSize}&name=`+ query+skillsString);
        setTotalCount(response.data.totalCount);
        return response.data.candidates;
    }
    else {
        let response = await axios.get(`http://localhost:9000/api/candidate/all?page=${activePage}&size=${pageSize}`);
        setTotalCount(response.data.totalCount);
        return response.data.candidates;
    }

}
async function sortData(
    data: RowData[],
    payload: { sortBy: keyof RowData | null; reversed: boolean; search: string;skills:string[],activePage:number,setTotalCount:any,pageSize:number }
) {
    const { sortBy } = payload;

    if (!sortBy) {
        return  await filterData(data, payload.search,payload.skills,payload.activePage,payload.setTotalCount,payload.pageSize);
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
        payload.search,payload.skills,payload.activePage,payload.setTotalCount,payload.pageSize
    );
}

export function UsersTable() {
    const [openedAddCandidate, { open, close }] = useDisclosure(false);
    const [skills,setSkills]=useState<SkillPreview[]>([]);
    const [activePage, setPage] = useState(1);
    const [skillModal, setSkillModal] = useState(false);
    const [id, setId] = useState(-1);
    const [totalCount, setTotalCount] = useState(10);
    const [data,setData]=useState<RowData[]>([]);
    const [search, setSearch] = useState('');
    const [selectedSkills, setSelectedSkills] = useState<string[]>([]);
    const [sortedData, setSortedData] = useState(data);
    const [sortBy, setSortBy] = useState<keyof RowData | null>(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);
    const [pageSize, setPageSize] = useState<string | null>('10');

    useEffect( ()=>{
        axios.get(`http://localhost:9000/api/candidate/all?page=${activePage}&size=${pageSize}`).then(response=>{
            setData(response.data.candidates)
        });
    },[activePage])
    useEffect( ()=>{
            sortData(data,{sortBy:"name",reversed:false,search,skills:selectedSkills,activePage:activePage-1,setTotalCount,pageSize:Number.parseInt(pageSize!)}).then(
                data=>{
                    setSortedData(data)
                }
            )
    },[data,pageSize]);
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
        sortData(data,{sortBy:"name",reversed:false,search,skills:selectedSkills,activePage:activePage-1,setTotalCount,pageSize:Number.parseInt(pageSize!)}).then(
            data=>{
                setSortedData(data)
            }
        )
    };
    useEffect(()=>{
        sortData(data,{sortBy:"name",reversed:false,search,skills:selectedSkills,activePage:activePage-1,setTotalCount,pageSize:Number.parseInt(pageSize!)}).then(
            data=>{
                setSortedData(data)
            }
        )
    },[search,selectedSkills]);
    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value } = event.currentTarget;
        setPage(1);
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
                <td><Tooltip label={makeTooltip(row.skills.slice(1,row.skills.length))}><span><Badge >{row.skills[0].name}</Badge>{row.skills.length>1? ""+"+"+(row.skills.length-1):""}</span></Tooltip></td>:<td>No skills</td>}
        </tr>
    ));
    return (
        <div className="content">
            <Modal className="modal" opened={openedAddCandidate} onClose={close} title="Manage Candidates" centered scrollAreaComponent={ScrollArea.Autosize}>
                <AddCandidateForm onClick={setData} id={id} onClose={close} />
            </Modal>
            <Modal opened={skillModal} className="modal" onClose={()=>{setSkillModal(false)}} title="Manage Skills" centered scrollAreaComponent={ScrollArea.Autosize}>
                <ManageSkills onClick={setData} />
            </Modal>
            <div className="buttons-div">
            <Button leftIcon={<AddIcon/>} onClick={()=>{setId(-1);open();}}>Add Candidate</Button>
            <Button leftIcon={<SettingsIcon/>} onClick={()=>{setSkillModal(true)}}>Manage Skills</Button>
            </div>

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
                    onChange={skills=>{setPage(1);setSelectedSkills(skills)}}
                />
                </div>
                <div className="page-size-div">
                    <Select className="page-size-select"
                            label="Items Per Page"
                            data={[
                                { value: '5', label: '5' },
                                { value: '10', label: '10' },
                                { value: '20', label: '20' },
                                { value: '100', label: '100' },
                            ]}
                            value={pageSize}
                            onChange={size=>{setPage(1);setPageSize(size)}}
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
            <Pagination className="paginator" value={activePage} onChange={setPage} total={Math.ceil(totalCount/Number.parseInt(pageSize!))} />

        </div>
    );
}
export default UsersTable;