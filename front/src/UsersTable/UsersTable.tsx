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
    rem,
} from '@mantine/core';
import { keys } from '@mantine/utils';
import { IconSelector, IconChevronDown, IconChevronUp, IconSearch } from '@tabler/icons-react';
import axios from "axios";

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

function filterData(data: RowData[], search: string) {
    const query = search.toLowerCase().trim();
    return data.filter((item) =>
        item.name.toLowerCase().includes(query)
    );
}

function sortData(
    data: RowData[],
    payload: { sortBy: keyof RowData | null; reversed: boolean; search: string }
) {
    const { sortBy } = payload;

    if (!sortBy) {
        return filterData(data, payload.search);
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
        payload.search
    );
}


export function UsersTable() {
    const [data,setData]=useState<RowData[]>([]);
    const [search, setSearch] = useState('');
    const [sortedData, setSortedData] = useState(data);
    const [sortBy, setSortBy] = useState<keyof RowData | null>(null);
    const [reverseSortDirection, setReverseSortDirection] = useState(false);
    useEffect( ()=>{
        axios.get("http://localhost:9000/api/candidate/all?page=0&size=10").then(response=>{
            setData(response.data.candidates)
        });
    },[])
    useEffect( ()=>{
            setSortedData(sortData(data,{sortBy:"name",reversed:false,search}))
    },[data])


    const setSorting = (field: keyof RowData) => {
        const reversed = field === sortBy ? !reverseSortDirection : false;
        setReverseSortDirection(reversed);
        setSortBy(field);
        setSortedData(sortData(data, { sortBy: field, reversed, search }));
    };

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { value } = event.currentTarget;
        setSearch(value);
        setSortedData(sortData(data, { sortBy, reversed: reverseSortDirection, search: value }));
    };

    const rows = sortedData.map((row) => (
        <tr key={row.name} onClick={()=>{console.log(row.name)}}>
            <td>{row.name}</td>
            <td>{row.email}</td>
            <td>{row.contactNumber}</td>
        </tr>
    ));

    return (
        <ScrollArea>
            <TextInput
                placeholder="Search by any field"
                mb="md"
                icon={<IconSearch size="0.9rem" stroke={1.5} />}
                value={search}
                onChange={handleSearchChange}
            />
            <Table horizontalSpacing="md" verticalSpacing="xs" miw={700} sx={{ tableLayout: 'fixed' }}>
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
                        Company
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
    );
}
export default UsersTable;