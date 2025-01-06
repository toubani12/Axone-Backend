import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { IEmployer } from '../employer.model';
import { EmployerService } from '../service/employer.service';
import { EmployerFormService } from './employer-form.service';

import { EmployerUpdateComponent } from './employer-update.component';

describe('Employer Management Update Component', () => {
  let comp: EmployerUpdateComponent;
  let fixture: ComponentFixture<EmployerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employerFormService: EmployerFormService;
  let employerService: EmployerService;
  let userService: UserService;
  let walletService: WalletService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, EmployerUpdateComponent],
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
      .overrideTemplate(EmployerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employerFormService = TestBed.inject(EmployerFormService);
    employerService = TestBed.inject(EmployerService);
    userService = TestBed.inject(UserService);
    walletService = TestBed.inject(WalletService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const employer: IEmployer = { id: 456 };
      const relatedUser: IUser = { id: 5594 };
      employer.relatedUser = relatedUser;

      const userCollection: IUser[] = [{ id: 8916 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [relatedUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employer });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call wallet query and add missing value', () => {
      const employer: IEmployer = { id: 456 };
      const wallet: IWallet = { id: 4191 };
      employer.wallet = wallet;

      const walletCollection: IWallet[] = [{ id: 31942 }];
      jest.spyOn(walletService, 'query').mockReturnValue(of(new HttpResponse({ body: walletCollection })));
      const expectedCollection: IWallet[] = [wallet, ...walletCollection];
      jest.spyOn(walletService, 'addWalletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employer });
      comp.ngOnInit();

      expect(walletService.query).toHaveBeenCalled();
      expect(walletService.addWalletToCollectionIfMissing).toHaveBeenCalledWith(walletCollection, wallet);
      expect(comp.walletsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employer: IEmployer = { id: 456 };
      const relatedUser: IUser = { id: 6048 };
      employer.relatedUser = relatedUser;
      const wallet: IWallet = { id: 26313 };
      employer.wallet = wallet;

      activatedRoute.data = of({ employer });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(relatedUser);
      expect(comp.walletsCollection).toContain(wallet);
      expect(comp.employer).toEqual(employer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployer>>();
      const employer = { id: 123 };
      jest.spyOn(employerFormService, 'getEmployer').mockReturnValue(employer);
      jest.spyOn(employerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employer }));
      saveSubject.complete();

      // THEN
      expect(employerFormService.getEmployer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employerService.update).toHaveBeenCalledWith(expect.objectContaining(employer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployer>>();
      const employer = { id: 123 };
      jest.spyOn(employerFormService, 'getEmployer').mockReturnValue({ id: null });
      jest.spyOn(employerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employer }));
      saveSubject.complete();

      // THEN
      expect(employerFormService.getEmployer).toHaveBeenCalled();
      expect(employerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployer>>();
      const employer = { id: 123 };
      jest.spyOn(employerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareWallet', () => {
      it('Should forward to walletService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(walletService, 'compareWallet');
        comp.compareWallet(entity, entity2);
        expect(walletService.compareWallet).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
