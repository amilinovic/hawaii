import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

class TeamItem extends Component {
  render() {
    const team = this.props.team;
    return (
      <tr>
        <td>
          <NavLink to={`teams/${team.id}`}>{team.name}</NavLink>
        </td>
        <td>test members</td>
        <td>{team.emails}</td>
      </tr>
    );
  }
}

export default TeamItem;
