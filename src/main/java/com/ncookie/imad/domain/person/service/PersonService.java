package com.ncookie.imad.domain.person.service;

import com.ncookie.imad.domain.contents.entity.MovieData;
import com.ncookie.imad.domain.contents.entity.TvProgramData;
import com.ncookie.imad.domain.person.dto.DetailsCredits;
import com.ncookie.imad.domain.person.dto.DetailsPerson;
import com.ncookie.imad.domain.person.entity.Credit;
import com.ncookie.imad.domain.person.entity.Person;
import com.ncookie.imad.domain.person.repository.CreditRepository;
import com.ncookie.imad.domain.person.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final CreditRepository creditRepository;

    public Person savePersonEntity(Person person) {
        return personRepository.save(person);
    }

    public void saveCredit(DetailsPerson credit, Person person, TvProgramData tvProgramData) {
        creditRepository.save(
                Credit.builder()
                        .creditId(credit.getCreditId())
                        .contents(tvProgramData)
                        .person(person)
                        .knownForDepartment(credit.getKnownForDepartment())
                        .department(credit.getDepartment())
                        .job(credit.getJob())
                        .characterName(credit.getCharacter())
                        .build());
    }

    public void saveCredit(DetailsPerson credit, Person person, MovieData movieData) {
        creditRepository.save(
                Credit.builder()
                        .creditId(credit.getCreditId())
                        .contents(movieData)
                        .person(person)
                        .knownForDepartment(credit.getKnownForDepartment())
                        .department(credit.getDepartment())
                        .job(credit.getJob())
                        .characterName(credit.getCharacter())
                        .build());
    }
}

