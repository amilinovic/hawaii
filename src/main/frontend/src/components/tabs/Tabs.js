import React from 'react';
import Tab from '../tabs/Tab';

const Tabs = ({ activeTabIndex, data, handleTabClick }) => (
  <div>
    <div className="d-flex flex-row flex-grow-1">
      {data.map(({ label }, index) => (
        <Tab
          key={index}
          label={label}
          isActive={activeTabIndex === index}
          handleTabClick={handleTabClick}
          tabIndex={index}
        />
      ))}
    </div>
  </div>
);

export default Tabs;
