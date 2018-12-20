import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components';

const BodyTr = styled.tr`
  width: 100%;
  background-color: #f9f9f9;
  padding: 10px 0px 10px 0px;
  border: 1px solid #f9f9f9;
  transition: 200ms ease;
  &:hover {
    border-color: #c6c6c7;
  }
  a {
    color: #fb4b4f;
    &:hover {
      color: red;
    }
  }
`;

const BodyTd = styled.td`
  padding: 8px 0px 8px 20px;
  margin-top: 10px;
`;

const SeparatorTr = styled.tr`
  height: 10px;
  background-color: transparent;
`;

class TeamItem extends Component {
  render() {
    const {
      team: { id, name, emails }
    } = this.props;
    return (
      <React.Fragment>
        <SeparatorTr />
        <BodyTr>
          <BodyTd>
            <NavLink to={`teams/${id}`}>{name}</NavLink>
          </BodyTd>
          <BodyTd>test members</BodyTd>
          <BodyTd>{emails}</BodyTd>
        </BodyTr>
      </React.Fragment>
    );
  }
}

export default TeamItem;
