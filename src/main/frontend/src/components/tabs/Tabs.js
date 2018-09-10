import React, { Component } from 'react';
import styled from 'styled-components';

import Tab from '../tabs/Tab';

const TabsWrapper = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
`;

const TabList = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
`;

class Tabs extends Component {
  handleClick = tab => {
    this.props.changeTab(tab);
  };

  render() {
    return (
      <TabsWrapper>
        <TabList>
          {this.props.tabList.map((tab, index) => {
            return (
              <Tab
                handleClick={() => this.handleClick(index)}
                key={tab.id}
                name={tab.name}
                isCurrent={this.props.currentTab === index}
              />
            );
          })}
        </TabList>
      </TabsWrapper>
    );
  }
}

export default Tabs;
