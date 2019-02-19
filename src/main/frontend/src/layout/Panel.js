import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { Route, Switch } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import styled from 'styled-components';
import Dashboard from '../components/dashboard/Dashboard';
import InformationHeader from '../components/header/InformationHeader';
import TopHeader from '../components/header/TopHeader';
import Sidebar from '../components/navigation/Sidebar';
import Administration from '../pages/Administration';
import CreateEmployee from '../pages/CreateEmployee';
import CreateTeam from '../pages/CreateTeam';
import EditEmployee from '../pages/EditEmployee';
import Leave from '../pages/Leave';
import LeaveHistory from '../pages/LeaveHistory';
import Team from '../pages/Team';
import TeamCalendar from '../pages/TeamCalendar';
import { requestUser } from '../store/actions/userActions';
import { getUser } from '../store/selectors';

const PanelContent = styled.div`
  overflow: auto;
`;

class Panel extends Component {
  componentDidMount() {
    // Will be removed after `/me` endpoint is created, so user info could be fetched and stored in userStore
    this.props.requestUser(sessionStorage.getItem('userEmail'));
  }

  render() {
    return (
      <Fragment>
        <div className="root-wrapper">
          <Sidebar />
          <div className="d-flex flex-column justify-content-start flex-grow-1">
            <TopHeader user={this.props.user} />
            <InformationHeader user={this.props.user} />
            <PanelContent className="d-flex flex-column flex-grow-1">
              <Switch>
                <Route path="/dashboard" component={Dashboard} />
                <Route path="/leave" component={Leave} />
                <Route path="/leave-history" component={LeaveHistory} />
                <Route path="/team-calendar" component={TeamCalendar} />
                <Route path="/administration" component={Administration} />
                <Route path="/teams/create" component={CreateTeam} />
                <Route path="/teams/:id" component={Team} />
                <Route path="/employee/:id/edit" component={EditEmployee} />
                <Route path="/employee/create" component={CreateEmployee} />
              </Switch>
            </PanelContent>
          </div>
        </div>
      </Fragment>
    );
  }
}

const mapStateToProps = state => ({
  user: getUser(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestUser
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Panel);
