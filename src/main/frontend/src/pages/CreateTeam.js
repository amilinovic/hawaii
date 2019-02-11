import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { createTeam } from '../store/actions/teamActions';
import { requestUsers } from '../store/actions/usersActions';
import { getUsers } from '../store/selectors';

class CreateTeam extends Component {
  state = {
    team: {
      name: '',
      teamApprovers: [],
      users: []
    }
  };

  componentDidMount() {
    this.props.requestUsers();
  }

  teamNameChange(event) {
    this.setState({
      team: {
        ...this.state.team,
        name: event.target.value
      }
    });
  }

  addUser(user) {
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
    if (!this.props.users) return null;

    const users = this.props.users.map(user => {
      return (
        <div
          key={user.id}
          className="align-items-center d-flex justify-content-between my-2"
        >
          <span>{user.fullName}</span>
          <div>
            <button onClick={() => this.addUser(user)} className="btn mr-2">
              Member
            </button>
            <button onClick={() => this.addApprover(user)} className="btn">
              Approver
            </button>
          </div>
        </div>
      );
    });

    const selectedUsers = this.state.team.users.map(user => {
      return <h6 key={user.id}>{user.fullName}</h6>;
    });

    const selectedApprovers = this.state.team.teamApprovers.map(user => {
      return <h6 key={user.id}>{user.fullName}</h6>;
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
        {users}
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
  users: getUsers(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createTeam, requestUsers }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(CreateTeam));
