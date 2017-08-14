#!/bin/sh




rm -f examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&N_committed=0)' -const Pexp=0.5,gamma=0.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 > examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&N_committed=0)' -const Pexp=0.5,gamma=0.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&N_committed=0)' -const Pexp=0.5,gamma=0.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&N_committed=0)' -const Pexp=0.5,gamma=0.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4,5>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&preference5=1&N_committed=0)' -const Pexp=0.5,gamma=0.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.0e=1.0.txt

rm -f examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&N_committed=0)' -const Pexp=0.5,gamma=0.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 > examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&N_committed=0)' -const Pexp=0.5,gamma=0.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&N_committed=0)' -const Pexp=0.5,gamma=0.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&N_committed=0)' -const Pexp=0.5,gamma=0.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4,5>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&preference5=1&N_committed=0)' -const Pexp=0.5,gamma=0.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=0.5e=1.0.txt

rm -f examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&N_committed=0)' -const Pexp=0.5,gamma=1.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 > examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&N_committed=0)' -const Pexp=0.5,gamma=1.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&N_committed=0)' -const Pexp=0.5,gamma=1.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&N_committed=0)' -const Pexp=0.5,gamma=1.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4,5>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&preference5=1&N_committed=0)' -const Pexp=0.5,gamma=1.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.0e=1.0.txt

rm -f examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&N_committed=0)' -const Pexp=0.5,gamma=1.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 > examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&N_committed=0)' -const Pexp=0.5,gamma=1.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&N_committed=0)' -const Pexp=0.5,gamma=1.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&N_committed=0)' -const Pexp=0.5,gamma=1.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4,5>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&preference5=1&N_committed=0)' -const Pexp=0.5,gamma=1.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=1.5e=1.0.txt

rm -f examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&N_committed=0)' -const Pexp=0.5,gamma=2.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 > examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&N_committed=0)' -const Pexp=0.5,gamma=2.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&N_committed=0)' -const Pexp=0.5,gamma=2.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&N_committed=0)' -const Pexp=0.5,gamma=2.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4,5>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&preference5=1&N_committed=0)' -const Pexp=0.5,gamma=2.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.0e=1.0.txt

rm -f examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&N_committed=0)' -const Pexp=0.5,gamma=2.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 > examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&N_committed=0)' -const Pexp=0.5,gamma=2.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&N_committed=0)' -const Pexp=0.5,gamma=2.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&N_committed=0)' -const Pexp=0.5,gamma=2.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.5e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4,5>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&preference5=1&N_committed=0)' -const Pexp=0.5,gamma=2.5,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=2.5e=1.0.txt

rm -f examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=3.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&N_committed=0)' -const Pexp=0.5,gamma=3.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 > examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=3.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&N_committed=0)' -const Pexp=0.5,gamma=3.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=3.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&N_committed=0)' -const Pexp=0.5,gamma=3.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=3.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&N_committed=0)' -const Pexp=0.5,gamma=3.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=3.0e=1.0.txt
PRISM_JAVAMAXMEM=16g bin/prism examples/games/CDM/experiments/prob_choose_best/cdm5032.smg -ex -pctl 'filter(range, <<0,1,2,3,4,5>> Pmax=? [F all_committed&all_prefer_1], sched=0&preference1=1&preference2=1&preference3=1&preference4=1&preference5=1&N_committed=0)' -const Pexp=0.5,gamma=3.0,eta=1.0,lambda=1.0,Q1=1.0,Q2=0.5,Q3=0.25 >> examples/games/CDM/experiments/prob_choose_best/results/res_from1_l=1.0g=3.0e=1.0.txt

