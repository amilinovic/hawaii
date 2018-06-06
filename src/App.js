import React, { Component } from 'react';
import palms from './palms.gif';
import './App.scss';

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={palms} className="palms" alt="palms" />
          <h1 className="App-title">Hello Hawaii</h1>
        </header>
      </div>
    );
  }
}

export default App;
