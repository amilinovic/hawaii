import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { navigateOut } from '../store/actions/navigateActions';
import { removeTeam, requestTeam } from '../store/actions/teamActions';
import { getTeam } from '../store/selectors';

class Team extends Component {
  componentDidMount() {
    this.props.requestTeam(this.props.match.params.id);
  }

  componentWillUnmount() {
    this.props.navigateOut();
  }

  test() {}

  render() {
    if (this.props.team) {
      const {
        team: { teamName, teamMembers = [], teamApprovers = [] }
      } = this.props;

      const members = teamMembers.map(user => {
        return <h5 key={user.id}>{user.fullName}</h5>;
      });

      const approvers = teamApprovers.map(approver => {
        return <h5 key={approver.id}>{approver.fullName}</h5>;
      });
      return (
        <div className="d-flex h-100 p-4 flex-column align-items-center">
          <h1 className="mb-3">{teamName}</h1>
          <div className="mb-4 text-center">
            <h2 className="mb-2">Team members</h2>
            {members}
          </div>
          <div className="mb-4 text-center">
            <h2 className="mb-2">Team approvers</h2>
            {approvers}
          </div>
          <button
            className="btn btn-danger"
            onClick={() =>
              this.props.removeTeam({ id: this.props.match.params.id })
            }
          >
            Delete
          </button>
        </div>
      );
    }
    return null;
  }
}

const mapStateToProps = state => ({
  team: getTeam(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestTeam, removeTeam, navigateOut }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Team);
