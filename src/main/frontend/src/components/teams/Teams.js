import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { NavLink } from 'react-router-dom';
import { requestTeams } from '../../store/actions/teamsActions';
import { getTeams } from '../../store/selectors';
import TeamItem from './TeamItem';

class Teams extends Component {
  componentDidMount() {
    this.props.requestTeams();
  }

  render() {
    const { teams } = this.props.teams;

    let teamItems;

    if (teams !== null) {
      teamItems = Object.keys(this.props.teams).map(key => {
        return <TeamItem key={key} team={this.props.teams[key]} />;
      });
    }

    return (
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-12">
            <NavLink to={'/teams/create-team'}>
              <input
                type="button"
                value="Create Team"
                className="btn btn-primary float-right"
              />
            </NavLink>
          </div>
        </div>
        <div className="row">
          <div className="col-md-12">
            <table className="table table-hover">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Members</th>
                  <th>Approvers</th>
                </tr>
              </thead>
              <tbody>{teamItems}</tbody>
            </table>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  teams: getTeams(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestTeams }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Teams);
