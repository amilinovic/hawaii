import React, { Component } from 'react';
import { connect } from 'react';

import TextFieldGroup from '../common/textFieldGroup';

class CreateTeam extends Component {
  state = {
    active: true,
    emails: '',
    name: ''
  };

  onSubmit = e => {
    e.preventDefault();
    console.log('submit');
  };

  onChange = e => {
    this.setState({
      [e.target.name]: e.target.value
    });
    console.log('change');
  };

  render() {
    return (
      <div className="container">
        <div className="row">
          <div className="col-md-8 m-auto">
            <form className="form-group" onSubmit={this.onSubmit}>
              <TextFieldGroup
                placeholder="Team Name"
                name="name"
                value={this.state.name}
                onChange={this.onChange}
              />
              <TextFieldGroup
                placeholder="Emails"
                name="emails"
                value={this.state.emails}
                onChange={this.onChange}
              />
              <input
                type="submit"
                value="Submit"
                className="btn btn-info btn-block mt-4"
              />
            </form>
          </div>
        </div>
      </div>
    );
  }
}

// const mapStateToProps = state => ({
//   team: getTeam(state)
// })

export default CreateTeam;
