/// <reference types="cypress" />
describe('register test', () => {
    beforeEach(() => {
    cy.visit('/register');
    });
  it('register succesfully a user', () => {
    
    cy.contains('Register').click()
    cy.intercept('POST', '/api/auth/register', {
      body: {
        lastName: "toto",
        firstName: "toto",
        email: "toto3@toto.com",
        password: "test!1234"
      },
    })

    cy.get('input[formControlName="firstName"]').type('did') // Add username
    cy.get('input[formControlName="lastName"]').type('test') // Add username
    cy.get('input[formControlName="email"]').type('did@fakemail.fr') // Add email
    cy.get('input[formControlName="password"]').type('toto') // Add password
    cy.get('button').click()
    cy.url().should('include', '/login')
  })
  it('register with user already registered', () => {
    
    cy.contains('Register').click()
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: {
        lastName: "toto",
        firstName: "toto",
        email: "toto3@toto.com",
        password: "test!1234"
      },
    })

    cy.get('input[formControlName="firstName"]').type('did') // Add username
    cy.get('input[formControlName="lastName"]').type('test') // Add username
    cy.get('input[formControlName="email"]').type('did@fakemail.fr') // Add email
    cy.get('input[formControlName="password"]').type('toto') // Add password
    cy.get('button').click()
    cy.contains('An error occurred') // Add error message
  })
});