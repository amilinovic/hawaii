import React from 'react';
import Tab from '../tabs/Tab';

const Tabs = ({ activeTabIndex, data, handleTabClick }) => (
  <div className="d-flex flex-row">
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
);

export default Tabs;
