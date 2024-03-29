import React, { Component } from 'react';
import Employees from '../components/employees/Employees';
import LeaveTypes from '../components/leave-types/LeaveTypes';
import LeaveProfiles from '../components/leaveProfiles/LeaveProfiles';
import PublicHolidays from '../components/public-holidays/PublicHolidays';
import Tabs from '../components/tabs/Tabs';
import Teams from '../components/teams/Teams';
import Years from '../components/years/Years';

export default class Administration extends Component {
  state = {
    tabList: [
      { label: 'Panel', content: <Years /> },
      { label: 'Teams', content: <Teams /> },
      { label: 'Employees', content: <Employees /> },
      { label: 'Public Holidays', content: <PublicHolidays /> },
      { label: 'Leave types', content: <LeaveTypes /> },
      { label: 'Leave profiles', content: <LeaveProfiles /> }
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
      <div className="d-flex flex-column p-4 flex-grow-1">
        <Tabs
          handleTabClick={this.handleTabClick}
          data={this.state.tabList}
          activeTabIndex={activeTabIndex}
        />
        <div className="flex-1 pt-4 flex-grow-1 d-flex">
          {activeItem.content}
        </div>
      </div>
    );
  }
}
