import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAppTest } from 'app/entities/app-test/app-test.model';
import { AppTestService } from 'app/entities/app-test/service/app-test.service';
import { CustomQuestionService } from '../service/custom-question.service';
import { ICustomQuestion } from '../custom-question.model';
import { CustomQuestionFormService } from './custom-question-form.service';

import { CustomQuestionUpdateComponent } from './custom-question-update.component';

describe('CustomQuestion Management Update Component', () => {
  let comp: CustomQuestionUpdateComponent;
  let fixture: ComponentFixture<CustomQuestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customQuestionFormService: CustomQuestionFormService;
  let customQuestionService: CustomQuestionService;
  let appTestService: AppTestService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, CustomQuestionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CustomQuestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomQuestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customQuestionFormService = TestBed.inject(CustomQuestionFormService);
    customQuestionService = TestBed.inject(CustomQuestionService);
    appTestService = TestBed.inject(AppTestService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppTest query and add missing value', () => {
      const customQuestion: ICustomQuestion = { id: 456 };
      const appTest: IAppTest = { id: 27190 };
      customQuestion.appTest = appTest;

      const appTestCollection: IAppTest[] = [{ id: 32751 }];
      jest.spyOn(appTestService, 'query').mockReturnValue(of(new HttpResponse({ body: appTestCollection })));
      const additionalAppTests = [appTest];
      const expectedCollection: IAppTest[] = [...additionalAppTests, ...appTestCollection];
      jest.spyOn(appTestService, 'addAppTestToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customQuestion });
      comp.ngOnInit();

      expect(appTestService.query).toHaveBeenCalled();
      expect(appTestService.addAppTestToCollectionIfMissing).toHaveBeenCalledWith(
        appTestCollection,
        ...additionalAppTests.map(expect.objectContaining),
      );
      expect(comp.appTestsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customQuestion: ICustomQuestion = { id: 456 };
      const appTest: IAppTest = { id: 4899 };
      customQuestion.appTest = appTest;

      activatedRoute.data = of({ customQuestion });
      comp.ngOnInit();

      expect(comp.appTestsSharedCollection).toContain(appTest);
      expect(comp.customQuestion).toEqual(customQuestion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomQuestion>>();
      const customQuestion = { id: 123 };
      jest.spyOn(customQuestionFormService, 'getCustomQuestion').mockReturnValue(customQuestion);
      jest.spyOn(customQuestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customQuestion }));
      saveSubject.complete();

      // THEN
      expect(customQuestionFormService.getCustomQuestion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customQuestionService.update).toHaveBeenCalledWith(expect.objectContaining(customQuestion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomQuestion>>();
      const customQuestion = { id: 123 };
      jest.spyOn(customQuestionFormService, 'getCustomQuestion').mockReturnValue({ id: null });
      jest.spyOn(customQuestionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customQuestion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customQuestion }));
      saveSubject.complete();

      // THEN
      expect(customQuestionFormService.getCustomQuestion).toHaveBeenCalled();
      expect(customQuestionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomQuestion>>();
      const customQuestion = { id: 123 };
      jest.spyOn(customQuestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customQuestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customQuestionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppTest', () => {
      it('Should forward to appTestService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appTestService, 'compareAppTest');
        comp.compareAppTest(entity, entity2);
        expect(appTestService.compareAppTest).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
