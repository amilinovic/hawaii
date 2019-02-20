import React, { Component } from 'react';
import Employees from '../components/employees/Employees';
import Tabs from '../components/tabs/Tabs';
import Teams from '../components/teams/Teams';

export default class Administration extends Component {
  state = {
    tabList: [
      { label: 'Teams', content: <Teams /> },
      { label: 'Employees', content: <Employees /> }
    ],
    activeTabIndex: 0
  };

  handleTabClick = index => {
    this.setState({ activeTabIndex: index });
  };

  render() {
    const { activeTabIndex } = this.state;
    const activeItem = this.state.tabList[activeTabIndex];
    return (
      <div className="d-flex flex-column p-4">
        <Tabs
          handleTabClick={this.handleTabClick}
          data={this.state.tabList}
          activeTabIndex={activeTabIndex}
        />
        <div className="flex-1 pt-4">{activeItem.content}</div>
      </div>
    );
  }
}
