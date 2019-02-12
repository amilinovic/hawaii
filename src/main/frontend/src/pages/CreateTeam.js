import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { requestEmployees } from '../store/actions/employeesAction';
import { createTeam } from '../store/actions/teamActions';
import { getEmployees } from '../store/selectors';

class CreateTeam extends Component {
  state = {
    team: {
      name: '',
      teamApprovers: [],
      users: []
    }
  };

  componentDidMount() {
    this.props.requestEmployees();
  }

  teamNameChange(event) {
    this.setState({
      team: {
        ...this.state.team,
        name: event.target.value
      }
    });
  }

  addMember(user) {
    this.setState(prevState => ({
      team: {
        ...this.state.team,
        users: [...prevState.team.users, user]
      }
    }));
  }

  addApprover(user) {
    this.setState(prevState => ({
      team: {
        ...this.state.team,
        teamApprovers: [...prevState.team.teamApprovers, user]
      }
    }));
  }

  render() {
    if (!this.props.employees) return null;

    const employees = this.props.employees.map(employee => {
      return (
        <div
          key={employee.id}
          className="align-items-center d-flex justify-content-between my-2"
        >
          <span>{employee.fullName}</span>
          <div>
            <button
              onClick={() => this.addMember(employee)}
              className="btn mr-2"
            >
              Member
            </button>
            <button onClick={() => this.addApprover(employee)} className="btn">
              Approver
            </button>
          </div>
        </div>
      );
    });

    const selectedUsers = this.state.team.users.map(employee => {
      return <h6 key={employee.id}>{employee.fullName}</h6>;
    });

    const selectedApprovers = this.state.team.teamApprovers.map(employee => {
      return <h6 key={employee.id}>{employee.fullName}</h6>;
    });

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <input
          type="text"
          value={this.state.team.name}
          onChange={e => this.teamNameChange(e)}
          placeholder="Team name"
          className="mb-3"
        />
        {employees}
        <div className="d-flex justify-content-between">
          <div className="mb-5">
            <h3>Team members</h3>
            {selectedUsers}
          </div>
          <div className="mb-5">
            <h3>Team approvers</h3>
            {selectedApprovers}
          </div>
        </div>
        <button
          onClick={() => this.props.createTeam(this.state.team)}
          className="btn"
        >
          Create
        </button>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  employees: getEmployees(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createTeam, requestEmployees }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateTeam));
