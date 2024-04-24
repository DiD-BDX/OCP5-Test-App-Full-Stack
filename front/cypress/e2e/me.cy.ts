/// <reference types="cypress" />
describe('me component', () => {
    //let adminRole = true
    beforeEach(() => {
        // Visit the login page
        cy.visit('/login')
        // Intercept the login request
        cy.intercept('POST', '/api/auth/login', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            //admin: adminRole
            },
        })
    
        // Intercept the session request
        cy.intercept('GET', '/api/session', []).as('session')
    
        // Intercept the me request
        cy.intercept('GET', '/api/me', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            //admin: adminRole
            },
        }).as('me')
    })

    it('should display admin information after login', () => {
      
      cy.intercept('GET', '/api/user/1', {
        body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        email: 'user@email.com',
        createdAt: '2021-09-01T00:00:00.000Z',
        updatedAt: '2021-09-01T00:00:00.000Z',
        admin: true
        },
        }).as('user')
  
      // Fill in the login form and submit
      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}`)
      cy.contains('Submit').click()
  
      // Click on the Account link to go to the me page
      cy.contains('Account').click()
  
      // Check that the URL includes '/me'
      cy.url().should('include', '/me')
  
      // Check that the user information is displayed
      cy.contains('firstName')
      cy.contains('LASTNAME')
      cy.contains('email')
    })

    it('should display user information after login', () => {
        
        cy.intercept('GET', '/api/user/1', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            email: 'user@email.com',
            createdAt: '2021-09-01T00:00:00.000Z',
            updatedAt: '2021-09-01T00:00:00.000Z',
            admin: false
            },
            }).as('user')

        // Fill in the login form and submit
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}`)
        cy.contains('Submit').click()
    
        // Click on the Account link to go to the me page
        cy.contains('Account').click()
    
        // Check that the URL includes '/me'
        cy.url().should('include', '/me')
    
        // Check that the user information is displayed
        cy.contains('firstName')
        cy.contains('LASTNAME')
        cy.contains('email')
        })
  })