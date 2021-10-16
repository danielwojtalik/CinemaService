package criteria;

public class SearchFactory {

    public Search getSearchAlgorithm(SearchCriteria searchCriteria) {
        if (SearchCriteria.BY_NAME == searchCriteria) {
            return new SearchByName();
        } else if (SearchCriteria.BY_SURNAME == searchCriteria) {
            return new SearchBySurname();
        }

        return new SearchByAge();

    }

}
