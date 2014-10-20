package bluefinderinuse



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BFRecommendationController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond BFRecommendation.list(params), model:[BFRecommendationInstanceCount: BFRecommendation.count()]
    }

    def show(BFRecommendation BFRecommendationInstance) {
        respond BFRecommendationInstance
    }

    def create() {
        respond new BFRecommendation(params)
    }

    @Transactional
    def save(BFRecommendation BFRecommendationInstance) {
        if (BFRecommendationInstance == null) {
            notFound()
            return
        }

        if (BFRecommendationInstance.hasErrors()) {
            respond BFRecommendationInstance.errors, view:'create'
            return
        }

        BFRecommendationInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'BFRecommendation.label', default: 'BFRecommendation'), BFRecommendationInstance.id])
                redirect BFRecommendationInstance
            }
            '*' { respond BFRecommendationInstance, [status: CREATED] }
        }
    }

    def edit(BFRecommendation BFRecommendationInstance) {
        respond BFRecommendationInstance
    }

    @Transactional
    def update(BFRecommendation BFRecommendationInstance) {
        if (BFRecommendationInstance == null) {
            notFound()
            return
        }

        if (BFRecommendationInstance.hasErrors()) {
            respond BFRecommendationInstance.errors, view:'edit'
            return
        }

        BFRecommendationInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'BFRecommendation.label', default: 'BFRecommendation'), BFRecommendationInstance.id])
                redirect BFRecommendationInstance
            }
            '*'{ respond BFRecommendationInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(BFRecommendation BFRecommendationInstance) {

        if (BFRecommendationInstance == null) {
            notFound()
            return
        }

        BFRecommendationInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'BFRecommendation.label', default: 'BFRecommendation'), BFRecommendationInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'BFRecommendation.label', default: 'BFRecommendation'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
