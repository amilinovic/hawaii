import React, { Component } from 'react';
import Tabs from '../tabs/Tabs';
import Teams from '../teams/Teams';
import Employees from '../employees/Employees';

class Dashboard extends Component {
  render() {
    return (
      <Tabs>
        <div label="Teams">
          <Teams />
        </div>
        <div label="Employees">
          <Employees />
        </div>
      </Tabs>
    );
  }
}

export default Dashboard;
