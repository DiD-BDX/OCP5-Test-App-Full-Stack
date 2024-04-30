/// <reference types="cypress" />
describe('detail session for admin user', () => {
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
            admin: false
            },
        })
        cy.intercept('GET', '/api/user/1', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'Toto',
            lastName: 'Non-Admin',
            email: 'usertoto@email.com',
            createdAt: '2021-09-01T00:00:00.000Z',
            updatedAt: '2021-09-01T00:00:00.000Z',
            admin: false
            },
            }).as('user')
    
        // Intercept the session request
        cy.intercept('GET', '/api/session', {
            body:[ {
              id: 2,
              name: "Toto Non-Admin",
              date: "2024-04-06T00:00:00.000+00:00",
              teacher_id: 2,
              description: "session yoga v2 v3",
              users: [1,3,5,6],
              createdAt: "2024-04-05T15:25:49",
              updatedAt: "2024-04-10T14:52:03"
            }],
            }).as('sessions')

            cy.intercept('GET', '/api/session/2', {
                body:{
                    id: 2,
                    name: "Toto Non-Admin",
                    date: "2024-04-06T00:00:00.000+00:00",
                    teacher_id: 2,
                    description: "session yoga v2 v3",
                    users: [1,3,5,6],
                    createdAt: "2024-04-05T15:25:49",
                    updatedAt: "2024-04-10T14:52:03"
                  },
                  }).as('session') 

            cy.intercept('GET', '/api/teacher/2', {
                body:{
                    id: 2,
                    lastName: "Admin",
                    firstName: "DiD",
                    createdAt: "2024-04-05T15:25:49",
                    updatedAt: "2024-04-10T14:52:03"
                },
                }).as('teacher') 
    
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        cy.url().should('include', '/sessions')
        
    })

    it('should display the detail page', () => {
        cy.contains('Detail').click()
        cy.url().should('include', '/sessions/detail/2')
    })
    
    it('should go back to the sessions page', () => {
        cy.contains('Detail').click()
        cy.get('button.mdc-icon-button:contains("arrow_back")').click()
        cy.url().should('include', '/sessions')
    })

    it('should unregistered in the session', () => {
        // Intercept the session request
        cy.intercept('GET', '/api/session', {
            body:[ {
              id: 2,
              name: "Toto Non-Admin",
              date: "2024-04-06T00:00:00.000+00:00",
              teacher_id: 2,
              description: "session yoga v2 v3",
              users: [1,3,5,6],
              createdAt: "2024-04-05T15:25:49",
              updatedAt: "2024-04-10T14:52:03"
            }],
            }).as('sessions')

            cy.intercept('GET', '/api/session/2', {
                body:{
                    id: 2,
                    name: "Toto Non-Admin",
                    date: "2024-04-06T00:00:00.000+00:00",
                    teacher_id: 2,
                    description: "session yoga v2 v3",
                    users: [1,3,5,6],
                    createdAt: "2024-04-05T15:25:49",
                    updatedAt: "2024-04-10T14:52:03"
                  },
            }).as('session') 

        cy.intercept('DELETE', '/api/session/2/participate/1', {
            statusCode: 200,
            body: {
                id: 2,
                name: "Toto Non-Admin",
                date: "2024-04-06T00:00:00.000+00:00",
                teacher_id: 2,
                description: "session yoga v2 v3",
                users: [3,5,6],
                createdAt: "2024-04-05T15:25:49",
                updatedAt: "2024-04-10T14:52:03"
            }
        }).as('unregister')


        cy.contains('Detail').click()
        cy.get('button:contains("Do not participate")').click()
    })

    it('should register in the session', () => {
        // Intercept the session request
        cy.intercept('GET', '/api/session', {
            body:[ {
              id: 2,
              name: "Toto Non-Admin",
              date: "2024-04-06T00:00:00.000+00:00",
              teacher_id: 2,
              description: "session yoga v2 v3",
              users: [3,5,6],
              createdAt: "2024-04-05T15:25:49",
              updatedAt: "2024-04-10T14:52:03"
            }],
            }).as('sessions')

            cy.intercept('GET', '/api/session/2', {
                body:{
                    id: 2,
                    name: "Toto Non-Admin",
                    date: "2024-04-06T00:00:00.000+00:00",
                    teacher_id: 2,
                    description: "session yoga v2 v3",
                    users: [3,5,6],
                    createdAt: "2024-04-05T15:25:49",
                    updatedAt: "2024-04-10T14:52:03"
                  },
                  }).as('session') 
        cy.intercept('POST', '/api/session/2/participate/1', {
            statusCode: 200,
            body: {
                id: 2,
                name: "Toto Non-Admin",
                date: "2024-04-06T00:00:00.000+00:00",
                teacher_id: 2,
                description: "session yoga v2 v3",
                users: [1,3,5,6],
                createdAt: "2024-04-05T15:25:49",
                updatedAt: "2024-04-10T14:52:03"
            }
        }).as('register')

        cy.contains('Detail').click()
        cy.get('button:contains("Participate")').click()
    })
})
