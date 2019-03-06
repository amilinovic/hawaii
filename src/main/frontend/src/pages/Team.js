import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { removeTeam, requestTeam } from '../store/actions/teamActions';
import { getTeam } from '../store/selectors';

class Team extends Component {
  componentDidMount() {
    this.props.requestTeam(this.props.match.params.id);
  }

  render() {
    if (!this.props.team) return null;

    const {
      team: {
        id,
        name,
        users = [],
        teamApprovers = [],
        sendEmailToTeammatesForBonusRequestEnabled,
        sendEmailToTeammatesForAnnualRequestEnabled,
        sendEmailToTeammatesForSicknessRequestEnabled,
        sicknessRequestEmails,
        annualRequestEmails,
        bonusRequestEmails
      }
    } = this.props;

    const members = users.map(user => {
      return (
        <h5 className="text-danger" key={user.id}>
          {user.fullName}
        </h5>
      );
    });

    const approvers = teamApprovers.map(approver => {
      return (
        <h5 className="text-danger" key={approver.id}>
          {approver.fullName}
        </h5>
      );
    });

    return (
      <div className="d-flex flex-grow-1 p-4 flex-column align-items-center">
        <h1>Team name</h1>
        <h5 className="text-danger mb-3">{name}</h5>
        <div className="mb-4 text-center">
          <h2 className="mb-2">Team members</h2>
          {members}
        </div>
        <div className="mb-4 text-center">
          <h2 className="mb-2">Team approvers</h2>
          {approvers}
        </div>
        <h1>Sickness request emails</h1>
        <h5 className="text-danger mb-3">{sicknessRequestEmails}</h5>
        <h1>Annual request emails</h1>
        <h5 className="text-danger mb-3">{annualRequestEmails}</h5>
        <h1>Bonus request emails</h1>
        <h5 className="text-danger mb-3">{bonusRequestEmails}</h5>
        <h1>Send email to teammates for annual request</h1>
        <h5 className="text-danger mb-3">
          {sendEmailToTeammatesForAnnualRequestEnabled ? 'Yes' : 'No'}
        </h5>
        <h1>Send email to teammates for bonus requests</h1>
        <h5 className="text-danger mb-3">
          {sendEmailToTeammatesForBonusRequestEnabled ? 'Yes' : 'No'}
        </h5>
        <h1>Send email to teammates for bonus request</h1>
        <h5 className="text-danger mb-3">
          {sendEmailToTeammatesForSicknessRequestEnabled ? 'Yes' : 'No'}
        </h5>
        <div>
          <button className="btn mr-3">
            <NavLink to={`/teams/${id}/edit`}>Edit</NavLink>
          </button>
          <button
            className="btn btn-danger"
            onClick={() =>
              this.props.removeTeam({ id: this.props.match.params.id })
            }
          >
            Delete
          </button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  team: getTeam(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestTeam, removeTeam }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(Team));
