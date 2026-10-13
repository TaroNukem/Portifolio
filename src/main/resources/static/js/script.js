const profileMenuButton = document.getElementById('profileMenuButton')
const profileDropdown = document.getElementById('profileDropdown')
const languageMenuButton = document.getElementById('languageMenuButton')
const languageSubmenu = document.getElementById('languageSubmenu')
const openPixButton = document.getElementById('openPixButton')
const pixModal = document.getElementById('pixModal')
const closePixButton = document.getElementById('closePixButton')

function closeLanguageMenu() {
    languageSubmenu.hidden = true
    languageMenuButton.setAttribute('aria-expanded', 'false')
}

function closeProfileMenu() {
    profileDropdown.hidden = true
    profileMenuButton.setAttribute('aria-expanded', 'false')
    closeLanguageMenu()
}

function closePixModal() {
    pixModal.hidden = true
    openPixButton.focus()
}

profileMenuButton.addEventListener('click', () => {
    if (profileDropdown.hidden) {
        profileDropdown.hidden = false
        profileMenuButton.setAttribute('aria-expanded', 'true')
    } else {
        closeProfileMenu()
    }
})

languageMenuButton.addEventListener('click', () => {
    const isOpen = !languageSubmenu.hidden
    languageSubmenu.hidden = isOpen
    languageMenuButton.setAttribute('aria-expanded', String(!isOpen))
})

openPixButton.addEventListener('click', () => {
    pixModal.hidden = false
    closeProfileMenu()
    closePixButton.focus()
})

closePixButton.addEventListener('click', closePixModal)

pixModal.addEventListener('click', event => {
    if (event.target === pixModal) {
        closePixModal()
    }
})

document.addEventListener('click', event => {
    if (!event.target.closest('.profile-menu')) {
        closeProfileMenu()
    }
})

document.addEventListener('keydown', event => {
    if (event.key === 'Escape') {
        closeProfileMenu()

        if (!pixModal.hidden) {
            closePixModal()
        }
    }
})

function syncTexts(current, next) {
    if (current.nodeType !== Node.ELEMENT_NODE) return

    if (current.tagName === 'SCRIPT' || current.tagName === 'VIDEO') return

    ['alt', 'aria-label', 'title', 'placeholder', 'data-network-error'].forEach(attr => {
        if (
            next.hasAttribute(attr) &&
            current.getAttribute(attr) !== next.getAttribute(attr)
        ) {
            current.setAttribute(attr, next.getAttribute(attr))
        }
    })

    if (current.classList.contains('language-option')) {
        current.className = next.className
    }

    const currentNodes = current.childNodes
    const nextNodes = next.childNodes

    if (currentNodes.length !== nextNodes.length) return

    for (let i = 0; i < currentNodes.length; i++) {
        const a = currentNodes[i]
        const b = nextNodes[i]

        if (
            a.nodeType === Node.TEXT_NODE &&
            b.nodeType === Node.TEXT_NODE
        ) {
            if (a.nodeValue !== b.nodeValue) {
                a.nodeValue = b.nodeValue
            }
        } else if (
            a.nodeType === Node.ELEMENT_NODE &&
            b.nodeType === Node.ELEMENT_NODE &&
            a.tagName === b.tagName
        ) {
            syncTexts(a, b)
        }
    }
}

async function switchLanguage(url) {
    const response = await fetch(url, {
        credentials: 'same-origin'
    })

    if (!response.ok) throw new Error('Falha ao trocar idioma')

    const html = await response.text()
    const newDoc = new DOMParser().parseFromString(html, 'text/html')

    document.documentElement.lang = newDoc.documentElement.lang
    document.title = newDoc.title

    syncTexts(document.body, newDoc.body)
}

document.querySelectorAll('.language-option').forEach(link => {
    link.addEventListener('click', async event => {
        event.preventDefault()

        const url = link.href

        try {
            await switchLanguage(url)
            closeProfileMenu()
        } catch (error) {
            window.location.href = url
        }
    })
})

const contactModal = document.getElementById('contactModal')
const openContactButton = document.getElementById('openContactButton')
const closeContactButton = document.getElementById('closeContactButton')
const contactForm = document.getElementById('contactForm')
const contactFeedback = document.getElementById('contactFeedback')
const contactSubmit = document.getElementById('contactSubmit')

function resetContactModal() {
    contactForm.reset()
    contactFeedback.textContent = ''
    contactFeedback.className = 'contact-feedback'
    contactSubmit.disabled = false
}

function closeContactModal() {
    contactModal.hidden = true
    resetContactModal()
    openContactButton.focus()
}

openContactButton.addEventListener('click', () => {
    resetContactModal()
    contactModal.hidden = false
    contactForm.elements.nome.focus()
})

closeContactButton.addEventListener('click', closeContactModal)

contactModal.addEventListener('click', e => {
    if (e.target === contactModal) closeContactModal()
})

document.addEventListener('keydown', e => {
    if (e.key === 'Escape' && !contactModal.hidden) closeContactModal()
})

contactForm.addEventListener('submit', async e => {
    e.preventDefault()
    if (!contactForm.reportValidity()) return

    contactSubmit.disabled = true
    contactFeedback.className = 'contact-feedback'
    contactFeedback.textContent = '...'

    try {
        const response = await fetch('/contato', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(Object.fromEntries(new FormData(contactForm)))
        })
        const data = await response.json()
        contactFeedback.textContent = data.message
        contactFeedback.classList.add(response.ok ? 'success' : 'error')
        if (response.ok) contactForm.reset()
    } catch (err) {
        contactFeedback.textContent = contactForm.dataset.networkError
        contactFeedback.classList.add('error')
    } finally {
        contactSubmit.disabled = false
    }
})