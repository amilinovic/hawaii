import { FieldArray } from 'formik';
import React, { Component } from 'react';
import { DebounceInput } from 'react-debounce-input';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import styled from 'styled-components';
import { searchEmployees } from '../../store/actions/employeesSearchActions';

const Results = styled.div`
  background: white;
  height: 150px;
  overflow: auto;
  display: ${props =>
    props.dropdownIsActive || props.inputIsActive ? 'block' : 'none'};
`;

const numberOfCharacters = 4;
class SearchDropdown extends Component {
  // TODO: Make this component more uniform since it's going to be used across the project
  state = {
    inputIsActive: false,
    dropdownIsActive: false
  };

  mouseEnter() {
    if (this.inputReference.value.length < numberOfCharacters) {
      this.setState({
        inputIsActive: false,
        dropdownIsActive: false
      });
    } else {
      this.setState({
        dropdownIsActive: true
      });
    }
  }

  mouseLeave() {
    if (this.inputReference.value.length < numberOfCharacters) {
      this.setState({
        inputIsActive: false,
        dropdownIsActive: false
      });
    } else {
      this.setState({
        dropdownIsActive: false,
        inputIsActive: true
      });
      this.inputReference.focus();
    }
  }

  mouseOver() {
    if (this.inputReference.value.length < numberOfCharacters) {
      this.setState({
        inputIsActive: false,
        dropdownIsActive: false
      });
    }
  }

  search() {
    this.checkIfInputHasEnoughCharacters();
    this.props.searchEmployees(this.inputReference.value);
  }

  checkIfInputHasEnoughCharacters() {
    if (this.inputReference.value.length >= numberOfCharacters) {
      this.setState({
        inputIsActive: true
      });
    } else {
      this.setState({
        inputIsActive: false,
        dropdownIsActive: false
      });
    }
  }

  render() {
    return (
      <div className="position-relative">
        <DebounceInput
          inputRef={ref => {
            this.inputReference = ref;
          }}
          onFocus={() => this.checkIfInputHasEnoughCharacters()}
          onBlur={() =>
            this.setState({
              inputIsActive: false
            })
          }
          minLength={numberOfCharacters}
          debounceTimeout={300}
          onChange={e => this.search(e)}
          placeholder="Users search (type 4 characters to search)"
          className="w-100 border"
        />
        <Results
          onMouseEnter={() => this.mouseEnter()}
          onMouseLeave={() => this.mouseLeave()}
          onMouseOver={() => this.mouseOver()}
          inputIsActive={this.state.inputIsActive}
          dropdownIsActive={this.state.dropdownIsActive}
          className="results position-absolute w-100 p-4 border border-top-0"
        >
          {this.props.results.isFetching ? (
            <span>Loading...</span>
          ) : !this.props.results.results ? null : !this.props.results
              .isFetching && !this.props.results.results.length ? (
            <span>No results</span>
          ) : (
            //   TODO: This part will be extracted to separate component based on the needs
            this.props.results.results.map(result => {
              return (
                <label
                  className="d-flex justify-content-between"
                  key={result.id}
                >
                  {result.fullName}
                  <div className="mb-2">
                    <FieldArray
                      name="users"
                      render={arrayHelpers => (
                        <button
                          className="btn mr-2"
                          type="button"
                          onClick={() => {
                            arrayHelpers.push(result);
                            this.inputReference.focus();
                          }}
                        >
                          Add member
                        </button>
                      )}
                    />
                    <FieldArray
                      name="teamApprovers"
                      render={arrayHelpers => (
                        <button
                          className="btn"
                          type="button"
                          onClick={() => {
                            arrayHelpers.push(result);
                            this.inputReference.focus();
                          }}
                        >
                          Add approver
                        </button>
                      )}
                    />
                  </div>
                </label>
              );
            })
          )}
        </Results>
      </div>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators({ searchEmployees }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(SearchDropdown);
