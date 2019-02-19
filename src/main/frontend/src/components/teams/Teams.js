import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import { requestTeams } from '../../store/actions/teamsActions';
import { getTeams } from '../../store/selectors';
import withResetOnNavigate from '../HOC/withResetOnNavigate';
import TeamItem from './TeamItem';
class Teams extends Component {
  componentDidMount() {
    this.props.requestTeams();
  }

  render() {
    if (!this.props.teams) return null;

    const teamItems = this.props.teams
      .filter(item => !item.deleted)
      .map(item => {
        return <TeamItem key={item.id} team={item} />;
      });

    return (
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-12">
            <NavLink to={'/administration'}>
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
)(withResetOnNavigate()(Teams));
