import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import {
  requestEmployee,
  updateEmployee
} from '../store/actions/employeeActions';
import { getEmployee } from '../store/selectors';
class EditEmployee extends Component {
  state = {
    employee: {
      email: '',
      fullName: '',
      jobTitle: '',
      leaveProfileId: 0,
      startedWorkingAtExecomDate: '',
      startedWorkingDate: '',
      teamId: 0,
      teamName: '',
      userRole: '',
      userStatusType: '',
      yearsOfService: 0
    }
  };

  componentDidMount() {
    this.props.requestEmployee(this.props.match.params.id);
  }

  static getDerivedStateFromProps(nextProps, prevState) {
    if (!nextProps.employee) {
      return null;
    } else if (
      !prevState.employee.fullName &&
      nextProps.employee !== prevState.employee
    ) {
      return { employee: nextProps.employee };
    }
    return null;
  }

  employeeNameChange(event) {
    this.setState({
      employee: {
        ...this.state.employee,
        fullName: event.target.value
      }
    });
  }

  employeeEmailChange(event) {
    this.setState({
      employee: {
        ...this.state.employee,
        email: event.target.value
      }
    });
  }

  employeeJobTitleChange(event) {
    this.setState({
      employee: {
        ...this.state.employee,
        jobTitle: event.target.value
      }
    });
  }

  render() {
    if (!this.props.employee) return null;

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <input
          type="text"
          defaultValue={this.props.employee.fullName}
          onChange={e => this.employeeNameChange(e)}
          placeholder="Employee name"
          className="mb-3"
        />
        <input
          type="text"
          defaultValue={this.props.employee.email}
          onChange={e => this.employeeEmailChange(e)}
          placeholder="Employee email"
          className="mb-3"
        />
        <input
          type="text"
          defaultValue={this.props.employee.jobTitle}
          onChange={e => this.employeeJobTitleChange(e)}
          placeholder="Job title"
          className="mb-3"
        />
        <button
          onClick={() => this.props.updateEmployee(this.state.employee)}
          className="btn"
        >
          Update
        </button>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  employee: getEmployee(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ updateEmployee, requestEmployee }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(EditEmployee));
