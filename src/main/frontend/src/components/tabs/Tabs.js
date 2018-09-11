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
      {data.map(({ label }, index) => {
        const isActive = activeTabIndex === index;
        return (
          <Tab
            key={index}
            label={label}
            isActive={isActive}
            handleTabClick={handleTabClick}
            tabIndex={index}
          />
        );
      })}
    </TabList>
  </TabsWrapper>
);

// class Tabs  {
//   handleClick = tab => {
//     this.props.changeTab(tab);
//   };

//   render() {
//     return (
//       <TabsWrapper>
//         <TabList>
//           {this.props.tabList.map((tab, index) => {
//             return (
//               <Tab
//                 handleClick={() => this.handleClick(index)}
//                 key={tab.id}
//                 name={tab.name}
//                 isCurrent={this.props.currentTab === index}
//               />
//             );
//           })}
//         </TabList>
//       </TabsWrapper>
//     );
//   }
// }

export default Tabs;
