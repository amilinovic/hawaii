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
      email: 'string',
      fullName: '',
      jobTitle: 'string',
      leaveProfileId: 0,
      startedWorkingAtExecomDate: 'string',
      startedWorkingDate: 'string',
      teamId: 0,
      teamName: 'string',
      userRole: 'HR_MANAGER',
      userStatusType: 'ACTIVE',
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

  render() {
    if (!this.props.employee) return null;

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <input
          type="text"
          defaultValue={this.state.employee.fullName}
          onChange={e => this.employeeNameChange(e)}
          placeholder="Employee name"
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
