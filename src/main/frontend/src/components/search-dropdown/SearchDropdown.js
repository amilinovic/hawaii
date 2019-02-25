import React, { Component } from 'react';
import styled from 'styled-components';
import { DebounceInput } from 'react-debounce-input';
import { FieldArray } from 'formik';
import { searchEmployees } from '../../store/actions/employeesActions';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

const Results = styled.div`
  background: white;
  height: 150px;
  overflow: auto;
  display: ${props =>
    props.dropdownIsActive || props.inputIsActive ? 'block' : 'none'};
`;
class SearchDropdown extends Component {
  state = {
    inputIsActive: false,
    dropdownIsActive: false
  };

  mouseEnter() {
    this.setState({
      dropdownIsActive: true
    });
  }

  mouseLeave() {
    this.setState({
      dropdownIsActive: false,
      inputIsActive: true
    });
    this.inputReference.focus();
  }
  render() {
    return (
      <div className="position-relative">
        <DebounceInput
          inputRef={ref => {
            this.inputReference = ref;
          }}
          onFocus={() =>
            this.setState({
              inputIsActive: true
            })
          }
          onBlur={() =>
            this.setState({
              inputIsActive: false
            })
          }
          minLength={4}
          debounceTimeout={300}
          onChange={e => this.props.searchEmployees(e.target.value)}
          placeholder="Users search"
          className="w-100"
        />

        <Results
          onMouseEnter={() => this.mouseEnter()}
          onMouseLeave={() => this.mouseLeave()}
          inputIsActive={this.state.inputIsActive}
          dropdownIsActive={this.state.dropdownIsActive}
          className="results position-absolute w-100 p-4 border border-top-0"
        >
          {!this.props.results.length ? (
            <span>Search...</span>
          ) : (
            this.props.results.map(result => {
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
