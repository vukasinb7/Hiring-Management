import React from 'react';
import logo from './logo.svg';
import './App.css';
import Header from "./Header/Header";
import UsersTable from "./UsersTable/UsersTable";


function App() {
  return (
    <div className="App">
      <Header/>
        <UsersTable/>
    </div>
  );
}

export default App;
