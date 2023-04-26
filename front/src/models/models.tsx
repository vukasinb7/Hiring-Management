

export interface SkillPreview{
    value:string;
    label:string;
}
export interface RowData {
    name: string;
    email: string;
    contactNumber: string;
    birth: string;
    skills:Skill[];
    id:number;
}

export interface Skill{
    name:string;
    id:number;
}

interface AllUsers{
    totalCount:number;
    candidates:RowData[];
}