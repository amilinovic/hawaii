import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

class TeamItem extends Component {
  render() {
    const team = this.props.team;
    console.log(team);
    return (
      <tr>
        <td>
          <NavLink to={`teams/${this.props.id}`}>{this.props.name}</NavLink>
        </td>
        <td>test members</td>
        <td>{this.props.emails}</td>
      </tr>
    );
  }
}

export default TeamItem;
