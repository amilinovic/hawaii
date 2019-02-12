import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { createEmployee } from '../store/actions/employeeActions';

class CreateEmployee extends Component {
  state = {
    employee: {
      teamId: 1,
      teamName: 'sasa matic',
      leaveProfileId: 1,
      fullName: 'Test name',
      email: 'test@execom.eu',
      userRole: 'HR_MANAGER',
      jobTitle: 'Developer',
      userStatusType: 'ACTIVE',
      startedWorkingDate: '2012-08-08',
      startedWorkingAtExecomDate: '2018-08-08'
    }
  };

  employeeNameChange(event) {
    this.setState({
      employee: {
        ...this.state.employee,
        fullName: event.target.value
      }
    });
  }
  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <input
          type="text"
          value={this.state.employee.fullName}
          onChange={e => this.employeeNameChange(e)}
          placeholder="Employee name"
          className="mb-3"
        />
        <button
          onClick={() => this.props.createEmployee(this.state.employee)}
          className="btn"
        >
          Create
        </button>
      </div>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createEmployee }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(withResetOnNavigate()(CreateEmployee));
