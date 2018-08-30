import React, { Component } from 'react';
import Button from '../common/button';
import Input from '../common/input';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { creatingTeam } from '../../store/actions/teamsActions';

class CreateTeam extends Component {
  state = {
    newTeam: {
      name: '',
      emails: ''
    }
  };

  handleEmails = e => {
    let value = e.target.value;
    let name = e.target.name;
    this.setState(prevState => ({
      newTeam: {
        ...prevState.newTeam,
        [name]: value
      }
    }));
  };

  handleName = e => {
    let value = e.target.value;
    let name = e.target.name;
    this.setState(prevState => ({
      newTeam: {
        ...prevState.newTeam,
        [name]: value
      }
    }));
  };

  handleFormSubmit = e => {
    e.preventDefault();
    let createdTeamData = this.state.newTeam;
    creatingTeam(createdTeamData, this.props.history);
  };

  handleClearForm = e => {
    e.preventDefault();
    this.setState({
      newTeam: {
        name: '',
        emails: ''
      }
    });
  };

  render() {
    return (
      <form className="container-fluid" onSubmit={this.handleFormSubmit}>
        <Input
          type={'text'}
          title={'Name'}
          name={'name'}
          value={this.state.newTeam.name}
          placeholder={'Enter name'}
          onChange={this.handleName}
        />
        <Input
          type={'text'}
          name={'emails'}
          title={'Emails'}
          value={this.state.newTeam.emails}
          placeholder={'Emails'}
          onChange={this.handleEmails}
        />
        <Button
          action={this.handleFormSubmit}
          type={'primary'}
          title={'Submit'}
        />{' '}
        {/*Submit */}
        <Button
          action={this.handleClearForm}
          type={'secondary'}
          title={'Clear'}
        />{' '}
        {/* Clear the form */}
      </form>
    );
  }
}

const mapStateToProps = state => ({
  team: state.team
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ creatingTeam }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withRouter(CreateTeam));
