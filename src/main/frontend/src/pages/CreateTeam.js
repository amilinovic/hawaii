import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { createTeam } from '../store/actions/teamActions';

class CreateTeam extends Component {
  addTeam = () => {
    console.log('added');
  };
  render() {
    return (
      <div className="d-flex p-4 justify-content-center">
        <button onClick={() => this.props.createTeam('test')} className="btn">
          Create{' '}
        </button>{' '}
      </div>
    );
  }
}

const mapStateToProps = state => ({
  teams: createTeam(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createTeam }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CreateTeam);
