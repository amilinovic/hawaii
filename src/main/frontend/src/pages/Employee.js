import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import {
  removeEmployee,
  requestEmployee
} from '../store/actions/employeeActions';
import { getEmployee } from '../store/selectors';

class Employee extends Component {
  componentDidMount() {
    this.props.requestEmployee(this.props.match.params.id);
  }

  render() {
    if (!this.props.employee) return null;
    const {
      employee: {
        id,
        teamName,
        fullName,
        email,
        jobTitle,
        yearsOfService,
        startedWorkingDate,
        startedWorkingAtExecomDate
      }
    } = this.props;

    return (
      <div className="d-flex h-100 p-4 flex-column align-items-center">
        <h1>Name</h1>
        <h5 className="text-danger mb-3">{fullName}</h5>
        <h1>email</h1>
        <h5 className="text-danger mb-3">{email}</h5>
        <h1>Job Title</h1>
        <h5 className="text-danger mb-3">{jobTitle}</h5>
        <h1>Years of service</h1>
        <h5 className="text-danger mb-3">{yearsOfService}</h5>
        <h1>Started working at execom</h1>
        <h5 className="text-danger mb-3">{startedWorkingDate}</h5>
        <h1>Started working date</h1>
        <h5 className="text-danger mb-3">{startedWorkingAtExecomDate}</h5>
        <h1>Team name</h1>
        <h5 className="text-danger mb-3">{teamName}</h5>
        <button
          className="btn btn-danger"
          onClick={() => this.props.removeEmployee({ id })}
        >
          Delete
        </button>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  employee: getEmployee(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestEmployee, removeEmployee }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(Employee));
