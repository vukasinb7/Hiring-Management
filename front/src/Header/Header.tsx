import './Header.css';
import {Button, Modal, ScrollArea} from "@mantine/core";
import {useDisclosure} from "@mantine/hooks";
import AddCandidateForm from "../AddCandidateForm/AddCandidateForm";
import ManageSkills from "../ManageSkills/ManageSkills";

function Header() {
    const [openedAddCandidate, { open, close }] = useDisclosure(false);
    return (

        <div className="header">
            <Modal className="modal" opened={openedAddCandidate} onClose={close} mih={600} title="Manage Skills" centered scrollAreaComponent={ScrollArea.Autosize}>
                <ManageSkills />
            </Modal>
            <div className="logo">
                <span>This is my logo</span>
            </div>
            <div className="buttons">

                <Button onClick={open}>Click me!</Button>
            </div>
        </div>
    );
}

export default Header;
