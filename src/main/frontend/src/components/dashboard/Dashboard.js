import React, { Component } from 'react';
import Tabs from '../tabs/Tabs';
import Employees from '../employees/Employees';
import Teams from '../teams/Teams';
import styled from 'styled-components';

const DashboardContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  padding: 20px;
`;

const TabContent = styled.div`
  flex: 1;
  width: 100%;
  padding-top: 16px;
`;

export default class Dashboard extends Component {
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
      <DashboardContainer>
        <Tabs
          handleTabClick={this.handleTabClick}
          data={this.state.tabList}
          activeTabIndex={activeTabIndex}
        />
        <TabContent>{activeItem.content}</TabContent>
      </DashboardContainer>
    );
  }
}
