package Tasks;

import java.util.List;

class Task extends Thread{
  private final Integer num;
  public List<Integer> primenumbers;

  Task(Integer num,List<Integer> primenumbers) {
    this.num = num;
    this.primenumbers=primenumbers;
  }

  public void run() {
    if(2==num) {
      primenumbers.add(2);                                  //since loop starts from integer 3,base case is done here
      return;
    }
    if(2<num)                                               //if number greater than 2,add 2 to prime numbers list
      primenumbers.add(2);
    for (int i = 3; i <= num; i++) {
      if (isprime(i)) {
        primenumbers.add(i);
      }
    }
  }

  private boolean isprime(int i){
    if (0==(i%2))                                         //if number is itself even,avoid entering into the loop
      return false;
    else{
      for (int k=3;k<=Math.sqrt(i);k++)                   //reducing the loop till square root of number
      {
        if(0==(i%k))
          return false;
      }
    }
    return true;
  }

}
