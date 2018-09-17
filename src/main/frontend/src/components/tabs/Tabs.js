import React from 'react';
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

const Tabs = ({ activeTabIndex, data, handleTabClick }) => (
  <TabsWrapper>
    <TabList>
      {data.map(({ label }, index) => (
        <Tab
          key={index}
          label={label}
          isActive={activeTabIndex === index}
          handleTabClick={handleTabClick}
          tabIndex={index}
        />
      ))}
    </TabList>
  </TabsWrapper>
);

export default Tabs;
