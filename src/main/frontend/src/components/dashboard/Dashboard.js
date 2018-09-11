import React, { Component } from 'react';
import Tabs from '../tabs/Tabs';
import Employees from '../employees/Employees';
import Teams from '../teams/Teams';
import Content from '../tabs/Content';

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
    // console.log(activeItem)
    return (
      <div>
        <Tabs
          handleTabClick={this.handleTabClick}
          data={this.state.tabList}
          activeTabIndex={activeTabIndex}
        />
        <Content content={activeItem.content} />
      </div>
    );
  }
}
