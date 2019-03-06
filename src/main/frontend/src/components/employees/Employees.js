import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import { requestEmployees } from '../../store/actions/employeesActions';
import { getEmployees } from '../../store/selectors';
import Loading from '../loading/Loading';
import EmployeeInfo from './EmployeeInfo';

class Employees extends Component {
  componentDidMount() {
    this.props.requestEmployees();
  }

  render() {
    if (!this.props.employees) return <Loading />;

    return (
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-12">
            <NavLink to={'/employee/create'}>
              <input
                type="button"
                value="Create Employee"
                className="btn btn-primary float-right"
              />
            </NavLink>
          </div>
        </div>
        <div className="row">
          <div className="col-md-12">
            {this.props.employees.map(employee => (
              <EmployeeInfo {...employee} key={employee.id} />
            ))}
          </div>
        </div>
      </div>
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
