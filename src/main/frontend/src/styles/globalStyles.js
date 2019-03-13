import { injectGlobal } from 'styled-components';

injectGlobal`
  @import url('https://fonts.googleapis.com/css?family=Montserrat');

*{
  user-select: none;	
}	

html, body {
  margin: 0;
  padding: 0;
  display: block;
  width: 100%;
  height: 100%;
}


body {	
  font-family: 'Montserrat', sans-serif;
  -webkit-font-smoothing: antialiased;
}

#root {
  width: 100%;
  height: 100%;
}

.root-wrapper {
  display: flex;
  flex-direction: row;
  height: 100%;
  width: 100%;
  overflow: hidden;
}

a {	
  text-decoration: none;	
  display: block;
  transition: 0.2s ease-in-out;
  &:hover,	
  &:focus,	
  &:active {	
    text-decoration: none;	
  }	
  &.active {
    background: #E45052;
  }
}	
	
button {	
  border: none;	
  &:hover,	
  &:focus,	
  &:active {	
    outline: none;	
  }	
}	
	
img {	
  max-width: 100%;	
}
	
textarea {	
  resize: none;	
  overflow-y: hidden;	
  &:focus,	
  &:active {	
    outline: none;	
  }	
}	

input {	
    &:focus,	
    &:active {	
      outline: none;	
    }	
  }	

// TODO: Move styling to the Radio Component

.radio {
  margin: 0.5rem;
  input[type="radio"] {
    position: absolute;
    opacity: 0;
    + .radio-label {
      &:before {
        content: '';
        background: #f4f4f4;
        border-radius: 100%;
        border: 1px solid darken(#f4f4f4, 25%);
        display: inline-block;
        width: 1.4em;
        height: 1.4em;
        position: relative;
        top: -0.2em;
        margin-right: 1em; 
        vertical-align: top;
        cursor: pointer;
        text-align: center;
        transition: all 250ms ease;
      }
    }
    &:checked {
      + .radio-label {
        &:before {
          background-color: #3197EE;
          box-shadow: inset 0 0 0 4px #f4f4f4;
        }
      }
    }
    &:focus {
      + .radio-label {
        &:before {
          outline: none;
          border-color: #3197EE;
        }
      }
    }
    &:disabled {
      + .radio-label {
        &:before {
          box-shadow: inset 0 0 0 4px #f4f4f4;
          border-color: darken(#f4f4f4, 25%);
          background: darken(#f4f4f4, 25%);
        }
      }
    }
    + .radio-label {
      &:empty {
        &:before {
          margin-right: 0;
        }
      }
    }
  }
}
`;
