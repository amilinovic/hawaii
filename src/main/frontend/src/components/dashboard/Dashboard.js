import React, { Component } from 'react';
import Teams from '../teams/Teams';

import Tabs from '../tabs/Tabs';

class Dashboard extends Component {
  render() {
    return (
      <Tabs>
        <div label="Teams">
          <Teams />
        </div>
        <div label="Employees">Employees</div>
      </Tabs>
    );
  }
}

export default Dashboard;
