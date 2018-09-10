import React, { Component } from 'react';
import styled from 'styled-components';
import Tabs from '../tabs/Tabs';
import Employees from '../employees/Employees';
import Teams from '../teams/Teams';

const TabContent = styled.div`
  flex: 1;
  width: 100%;
  padding-top: 16px;
`;

class Dashboard extends Component {
  state = {
    tabList: [
      { id: 1, name: 'Teams', content: <Teams /> },
      { id: 2, name: 'Employees', content: <Employees /> }
    ],
    currentTab: 0
  };

  changeTab = index => {
    this.setState({ currentTab: index });
  };

  render() {
    return (
      <div>
        <Tabs
          currentTab={this.state.currentTab}
          tabList={this.state.tabList}
          changeTab={this.changeTab}
        />
        <TabContent>
          {this.state.tabList[this.state.currentTab].content}
        </TabContent>
      </div>
    );
  }
}

export default Dashboard;
