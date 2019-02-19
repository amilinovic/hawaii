import React, { Component } from 'react';
import { connect } from 'react-redux';
import { requestEmployees } from '../../store/actions/employeesAction';
import { bindActionCreators } from 'redux';
import { getEmployees } from '../../store/selectors';
import styled from 'styled-components';
import EmployeeInfo from './EmployeeInfo';

const EmployeesContainer = styled.div`
  width: 100%;
  height: 100%;
`;

class Employees extends Component {
  componentDidMount() {
    this.props.requestEmployees();
  }
  render() {
    return (
      <EmployeesContainer>
        {this.props.employees &&
          this.props.employees.map(employee => (
            <EmployeeInfo {...employee} key={employee.id} />
          ))}
      </EmployeesContainer>
    );
  }
}

const mapStateToProps = state => ({
  employees: getEmployees(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestEmployees }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Employees);
