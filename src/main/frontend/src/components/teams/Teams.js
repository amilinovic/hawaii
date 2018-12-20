import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import Button from '../common/Button';
import { requestTeams } from '../../store/actions/teamsActions';
import { getTeams } from '../../store/selectors';
import styled from 'styled-components';
import TeamsTable from './TeamsTable';

const TeamsContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  padding: 30px;
`;

const ButtonContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  padding-right: 20px;
`;

class Teams extends Component {
  componentDidMount() {
    this.props.requestTeams();
  }

  render() {
    return (
      <TeamsContainer>
        <ButtonContainer>
          <Button
            click={() => {
              console.log('Button for team creation');
            }}
            title="Create Team"
          />
        </ButtonContainer>
        {this.props.teams.length > 0 && <TeamsTable teams={this.props.teams} />}
      </TeamsContainer>
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
